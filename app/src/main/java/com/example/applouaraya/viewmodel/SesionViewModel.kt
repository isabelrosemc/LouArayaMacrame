package com.example.applouaraya.viewmodel

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

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _registroState = MutableStateFlow(RegistroUiState())
    val registroState: StateFlow<RegistroUiState> = _registroState.asStateFlow()

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

        if (claveLimpia.length < 8) {
            _mensaje.value = "La contraseña debe tener al menos 8 caracteres"
            return
        }

        viewModelScope.launch {
            try {
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

    fun registrar(
        nombre: String,
        alias: String,
        correo: String,
        telefono: String,
        direccion: String,
        clave: String,
        confirmarClave: String,
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
                    _mensaje.value = err.message ?: "Error al registrar"
                }
            } catch (e: Exception) {
                _mensaje.value = "Error al registrar: ${e.message}"
            }
        }
    }

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

    fun onNombreChange(valor: String) {
        _registroState.value = _registroState.value.copy(
            nombre = valor,
            errorNombre = if (valor.isBlank()) "El nombre es obligatorio" else null
        )
    }

    fun onAliasChange(valor: String) {
        _registroState.value = _registroState.value.copy(
            alias = valor,
            errorAlias = if (valor.isBlank()) "El alias es obligatorio" else null
        )
    }

    fun onCorreoChange(valor: String) {
        val error = when {
            valor.isBlank() -> "El correo es obligatorio"
            !valor.contains("@") -> "Formato de correo no válido"
            else -> null
        }
        _registroState.value = _registroState.value.copy(correo = valor, errorCorreo = error)
    }

    fun onTelefonoChange(valor: String) {
        _registroState.value = _registroState.value.copy(telefono = valor)
    }

    fun onDireccionChange(valor: String) {
        _registroState.value = _registroState.value.copy(direccion = valor)
    }

    fun onContrasenaChange(valor: String) {
        val error = when {
            valor.isBlank() -> "La contraseña es obligatoria"
            valor.length < 8 -> "Debe tener al menos 8 caracteres"
            else -> null
        }
        _registroState.value = _registroState.value.copy(
            contrasena = valor,
            errorContrasena = error
        )
    }

    fun onConfirmarContrasenaChange(valor: String) {
        val error = when {
            valor.isBlank() -> "Debes confirmar la contraseña"
            valor != _registroState.value.contrasena -> "Las contraseñas no coinciden"
            else -> null
        }
        _registroState.value = _registroState.value.copy(
            confirmarContrasena = valor,
            errorConfirmarContrasena = error
        )
    }

    fun limpiarEstadoRegistro() {
        _registroState.value = RegistroUiState()
    }
}