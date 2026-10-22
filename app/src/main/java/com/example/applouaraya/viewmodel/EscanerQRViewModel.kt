package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.repository.LocalRepository
import com.example.applouaraya.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EscanerQRViewModel : ViewModel() {
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()
    private var repo: LocalRepository? = null
    fun configurar(r: LocalRepository) { repo = r }
    fun validar(codigo: String, clienteId: Int?, irDetalle: (Int) -> Unit) {
        val id = Regex("^LOU-PRODUCTO-([0-9]+)$").matchEntire(codigo.trim())
            ?.groupValues?.get(1)?.toIntOrNull()
        if (id == null || ProductoRepository().obtenerProductos().none { it.id == id }) {
            _mensaje.value = "Código QR desconocido o producto inexistente"
            return
        }
        if (clienteId != null) {
            viewModelScope.launch {
                try { repo?.registrarQR(clienteId, id) }
                catch (_: Exception) { _mensaje.value = "No se pudo guardar la lectura QR" }
            }
        }
        irDetalle(id)
    }
    fun error(m: String) { _mensaje.value = m }
}
