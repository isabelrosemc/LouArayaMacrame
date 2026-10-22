package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SesionViewModel : ViewModel() {
    private val _clienteId = MutableStateFlow<Int?>(null)
    val clienteId = _clienteId.asStateFlow()
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()
    private var repo: LocalRepository? = null
    fun configurar(r: LocalRepository) { repo = r }
    fun registrar(nombre: String, alias: String, correo: String, telefono: String,
        direccion: String, clave: String, alTerminar: () -> Unit) {
        if (nombre.isBlank() || alias.trim().length < 3 || clave.length < 8 ||
            !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches() ||
            (telefono.isNotBlank() && !Regex("^\\+?[0-9]{8,12}$").matches(telefono))) {
            _mensaje.value = "Nombre, alias (mínimo 3), correo, contraseña (mínimo 8) o teléfono inválido"
            return
        }
        val r = repo ?: return
        viewModelScope.launch {
            val resultado = r.registrar(nombre, alias, correo, telefono, direccion, clave)
            if (resultado.isSuccess) { _mensaje.value = "Cuenta registrada"; alTerminar() }
            else _mensaje.value = "No se pudo registrar: alias repetido u otro error"
        }
    }
    fun entrar(alias: String, clave: String) {
        if (alias.isBlank() || clave.isBlank()) { _mensaje.value = "Completa alias y contraseña"; return }
        val r = repo ?: return
        viewModelScope.launch {
            try {
                val cliente = r.entrar(alias, clave)
                if (cliente == null) _mensaje.value = "Credenciales incorrectas"
                else { _mensaje.value = ""; _clienteId.value = cliente.id }
            } catch (_: Exception) { _mensaje.value = "Error de inicio de sesión" }
        }
    }
    fun salir() { _clienteId.value = null; _mensaje.value = "" }
}
