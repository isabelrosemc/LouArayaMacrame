package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.data.local.SolicitudEntity
import com.example.applouaraya.repository.LocalRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

data class Cotizacion(val productoId: Int? = null, val evento: String = "", val medidas: String = "",
    val material: String = "", val colores: String = "", val fecha: String = "", val observaciones: String = "")
class CotizacionViewModel : ViewModel() {
    private val _datos = MutableStateFlow(Cotizacion())
    val datos = _datos.asStateFlow()
    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()
    private val _borradores = MutableStateFlow<List<SolicitudEntity>>(emptyList())
    val borradores = _borradores.asStateFlow()
    private var repo: LocalRepository? = null
    private var cliente: Int? = null
    private var trabajo: Job? = null
    fun configurar(r: LocalRepository) { repo = r }
    fun seleccionar(id: Int?) {
        trabajo?.cancel(); cliente = id; _borradores.value = emptyList()
        val r = repo ?: return
        if (id != null) trabajo = viewModelScope.launch {
            r.solicitudes(id).collect { _borradores.value = it }
        }
    }
    fun preparar(id: Int?) { _datos.value = Cotizacion(productoId = id); _mensaje.value = "" }
    fun actualizar(datos: Cotizacion) { _datos.value = datos; _mensaje.value = "" }
    fun guardar() {
        val d = _datos.value
        val fechaValida = try { LocalDate.parse(d.fecha).isAfter(LocalDate.now()) } catch (_: Exception) { false }
        if (d.evento.isBlank() || d.medidas.isBlank() || d.material.isBlank() || d.colores.isBlank() || !fechaValida) {
            _mensaje.value = "Completa todos los campos obligatorios e indica una fecha futura (AAAA-MM-DD)."
            return
        }
        val c = cliente
        val r = repo
        if (c == null || r == null) { _mensaje.value = "Debes iniciar sesión"; return }
        viewModelScope.launch {
            try {
                r.guardarSolicitud(SolicitudEntity(clienteId = c, productoId = d.productoId,
                    tipoEvento = d.evento, medidas = d.medidas, material = d.material,
                    colores = d.colores, fecha = d.fecha, observaciones = d.observaciones))
                _mensaje.value = "Borrador guardado en Room. No enviado al servidor."
            } catch (_: Exception) { _mensaje.value = "No fue posible guardar el borrador" }
        }
    }
}
