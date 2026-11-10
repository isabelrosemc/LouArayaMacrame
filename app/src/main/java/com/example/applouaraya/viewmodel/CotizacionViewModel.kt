package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.data.local.SolicitudEntity
import com.example.applouaraya.repository.LocalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CotizacionFormState(
    val productoId: Int? = null,
    val evento: String = "Matrimonio",
    val medidas: String = "",
    val material: String = "Algodón natural",
    val colores: String = "Crudo natural",
    val fecha: String = "",
    val observaciones: String = ""
)

class CotizacionViewModel : ViewModel() {

    private var repo: LocalRepository? = null
    private var clienteActualId: Int? = null

    // Formulario actual
    private val _datos = MutableStateFlow(CotizacionFormState())
    val datos: StateFlow<CotizacionFormState> = _datos.asStateFlow()

    // Lista de solicitudes (y alias borradores para compatibilidad)
    private val _solicitudes = MutableStateFlow<List<SolicitudEntity>>(emptyList())
    val solicitudes: StateFlow<List<SolicitudEntity>> = _solicitudes.asStateFlow()
    val borradores: StateFlow<List<SolicitudEntity>> = _solicitudes.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    fun configurar(localRepo: LocalRepository) {
        this.repo = localRepo
    }

    fun seleccionar(clienteId: Int?) {
        this.clienteActualId = clienteId
        val r = repo ?: return

        viewModelScope.launch {
            if (clienteId == 9999) {
                // Admin observa todas las solicitudes
                r.todasLasSolicitudes().collect { lista ->
                    _solicitudes.value = lista
                }
            } else if (clienteId != null) {
                // Cliente regular observa sus propias solicitudes
                r.solicitudes(clienteId).collect { lista ->
                    _solicitudes.value = lista
                }
            } else {
                _solicitudes.value = emptyList()
            }
        }
    }

    fun preparar(productoId: Int?) {
        _datos.value = _datos.value.copy(productoId = productoId)
        _mensaje.value = ""
    }

    fun actualizar(transform: (CotizacionFormState) -> CotizacionFormState) {
        _datos.value = transform(_datos.value)
    }

    fun guardar(onExito: () -> Unit = {}) {
        val cid = clienteActualId ?: 1
        val r = repo
        val f = _datos.value

        viewModelScope.launch {
            try {
                val nueva = SolicitudEntity(
                    clienteId = cid,
                    productoId = f.productoId,
                    tipoEvento = f.evento.ifBlank { "Personalizado" },
                    medidas = f.medidas.ifBlank { "A definir" },
                    material = f.material.ifBlank { "Algodón natural" },
                    colores = f.colores.ifBlank { "Crudo" },
                    fecha = f.fecha.ifBlank { "Por coordinar" },
                    observaciones = f.observaciones,
                    estado = "Pendiente de aprobación"
                )
                r?.guardarSolicitud(nueva)
                _mensaje.value = "Solicitud guardada con éxito"
                _datos.value = CotizacionFormState() // Limpiar formulario
                onExito()
            } catch (e: Exception) {
                _mensaje.value = "Error al guardar: ${e.message}"
            }
        }
    }

    fun cambiarEstado(solicitudId: Int, nuevoEstado: String) {
        viewModelScope.launch {
            try {
                repo?.cambiarEstadoSolicitud(solicitudId, nuevoEstado)
                _mensaje.value = "Solicitud #$solicitudId marcada como: $nuevoEstado"
            } catch (e: Exception) {
                _mensaje.value = "Error al actualizar estado: ${e.message}"
            }
        }
    }
}