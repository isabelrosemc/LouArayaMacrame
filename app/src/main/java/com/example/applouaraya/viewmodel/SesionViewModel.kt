package com.example.applouaraya.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.data.local.ClienteEntity
import com.example.applouaraya.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class RegistroUiState(
    val nombre: String = "",
    val alias: String = "",
    val correo: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val errorNombre: String? = null,
    val errorAlias: String? = null,
    val errorCorreo: String? = null,
    val errorContrasena: String? = null,
    val errorConfirmarContrasena: String? = null,
    val mensajeGeneral: String? = null,
    val registroExitoso: Boolean = false
)

class SesionViewModel(
    private var localRepository: LocalRepository? = null
) : ViewModel() {

    fun configurar(repo: LocalRepository) {
        this.localRepository = repo
    }

    private val _clienteAutenticado = MutableStateFlow<ClienteEntity?>(null)
    val clienteAutenticado: StateFlow<ClienteEntity?> = _clienteAutenticado.asStateFlow()

    val clienteId: StateFlow<Int?> = _clienteAutenticado
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Identificador para validar si el rol activo es Administrador
    val esAdmin: StateFlow<Boolean> = _clienteAutenticado
        .map { it?.alias?.equals("admin", ignoreCase = true) == true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _registroState = MutableStateFlow(RegistroUiState())
    val registroState: StateFlow<RegistroUiState> = _registroState.asStateFlow()

    fun esCorreoValido(correo: String): Boolean {
        return correo.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()
    }

    // Iniciar sesión consultando estrictamente a LocalRepository (Room o Admin)
    fun entrar(alias: String, clave: String) {
        val repo = localRepository
        if (repo == null) {
            _mensaje.value = "Repositorio local no disponible"
            return
        }

        val aliasLimpio = alias.trim()
        val claveLimpia = clave.trim()

        if (aliasLimpio.isBlank() || claveLimpia.isBlank()) {
            _mensaje.value = "Ingresa alias y contraseña"
            return
        }

        viewModelScope.launch {
            try {
                // Consulta real en Room con PasswordUtils.verificar(...)
                val cliente = repo.entrar(aliasLimpio, claveLimpia)
                if (cliente != null) {
                    _clienteAutenticado.value = cliente
                    _mensaje.value = ""
                } else {
                    _mensaje.value = "Alias o contraseña incorrectos"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al iniciar sesión: ${e.message}"
            }
        }
    }

    fun salir() {
        _clienteAutenticado.value = null
        _mensaje.value = ""
    }

    fun cerrarSesion() {
        salir()
    }

    // Registro persistido directamente en SQLite / Room mediante LocalRepository
    fun registrar(
        nombre: String,
        alias: String,
        correo: String,
        telefono: String,
        direccion: String,
        clave: String,
        confirmarClave: String = "",
        onExito: () -> Unit
    ) {
        val repo = localRepository
        if (repo == null) {
            _mensaje.value = "Repositorio no disponible"
            return
        }

        if (nombre.isBlank() || alias.isBlank() || correo.isBlank() || clave.isBlank()) {
            _mensaje.value = "Completa todos los campos obligatorios"
            return
        }

        if (!esCorreoValido(correo)) {
            _mensaje.value = "Ingresa un correo electrónico válido"
            return
        }

        if (clave.length < 8) {
            _mensaje.value = "La contraseña debe tener al menos 8 caracteres"
            return
        }

        if (confirmarClave.isNotBlank() && clave != confirmarClave) {
            _mensaje.value = "Las contraseñas no coinciden"
            return
        }

        viewModelScope.launch {
            try {
                val resultado = repo.registrar(
                    nombre = nombre.trim(),
                    alias = alias.trim(),
                    correo = correo.trim(),
                    telefono = telefono.trim(),
                    direccion = direccion.trim(),
                    clave = clave.trim()
                )

                resultado.onSuccess {
                    val cliente = repo.entrar(alias.trim(), clave.trim())
                    _clienteAutenticado.value = cliente
                    _mensaje.value = ""
                    onExito()
                }.onFailure { err ->
                    _mensaje.value = err.message ?: "Error al registrar cliente"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al registrar: ${e.message}"
            }
        }
    }

    // Actualización de datos de perfil en memoria (Nombre, Correo, Teléfono, Dirección)
    fun actualizarPerfil(
        nombre: String,
        correo: String,
        telefono: String,
        direccion: String,
        onResultado: (Boolean) -> Unit
    ) {
        val clienteActual = _clienteAutenticado.value
        if (clienteActual == null) {
            _mensaje.value = "No hay usuario activo"
            onResultado(false)
            return
        }

        if (!esCorreoValido(correo)) {
            _mensaje.value = "Formato de correo electrónico inválido"
            onResultado(false)
            return
        }

        val clienteActualizado = clienteActual.copy(
            nombre = nombre.trim(),
            correo = correo.trim(),
            telefono = telefono.trim(),
            direccion = direccion.trim()
        )
        _clienteAutenticado.value = clienteActualizado
        _mensaje.value = "Perfil actualizado correctamente"
        onResultado(true)
    }

    // Cambio de contraseña con actualización y persistencia real en Room
    fun cambiarContrasena(
        actualClave: String,
        nuevaClave: String,
        confirmarNuevaClave: String,
        onResultado: (Boolean, String) -> Unit
    ) {
        val repo = localRepository
        val clienteActual = _clienteAutenticado.value

        if (repo == null || clienteActual == null) {
            onResultado(false, "No hay sesión activa o repositorio disponible")
            return
        }

        // Restricción de negocio: la cuenta de administrador fija no altera clave aquí
        if (clienteActual.alias.equals("admin", ignoreCase = true)) {
            onResultado(false, "La cuenta de Administrador no permite cambiar su contraseña")
            return
        }

        val actual = actualClave.trim()
        val nueva = nuevaClave.trim()
        val confirmar = confirmarNuevaClave.trim()

        if (actual.isBlank() || nueva.isBlank() || confirmar.isBlank()) {
            onResultado(false, "Debes completar todos los campos")
            return
        }

        if (nueva.length < 8) {
            onResultado(false, "La nueva contraseña debe tener al menos 8 caracteres")
            return
        }

        if (nueva != confirmar) {
            onResultado(false, "Las nuevas contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            try {
                // 1. Validar que la contraseña actual ingresada coincida en la base de datos
                val clienteVerificado = repo.entrar(clienteActual.alias, actual)
                if (clienteVerificado == null) {
                    onResultado(false, "La contraseña actual es incorrecta")
                    return@launch
                }

                // 2. Persistir el nuevo Salt y Hash criptográfico en Room
                val resultado = repo.actualizarContrasena(clienteActual.id, nueva)

                resultado.onSuccess {
                    // 3. Recargar el cliente desde la base de datos para refrescar la sesión activa
                    val clienteActualizado = repo.entrar(clienteActual.alias, nueva)
                    _clienteAutenticado.value = clienteActualizado
                    _mensaje.value = "Contraseña actualizada exitosamente"
                    onResultado(true, "Contraseña actualizada exitosamente")
                }.onFailure { error ->
                    onResultado(false, "Error al guardar en base de datos: ${error.message}")
                }
            } catch (e: Exception) {
                onResultado(false, "Error al actualizar contraseña: ${e.message}")
            }
        }
    }
}