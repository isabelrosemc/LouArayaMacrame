package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.applouaraya.model.Producto
import com.example.applouaraya.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductoViewModel(
    private val repository: ProductoRepository = ProductoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState(isLoading = true))
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    // Se mantiene para que las pantallas existentes sigan funcionando sin cambios
    val productos: StateFlow<List<Producto>> = uiState
        .map { it.productos }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _categoria = MutableStateFlow("Todas")
    val categoria = _categoria.asStateFlow()

    init { cargarProductos() }

    fun buscar(valor: String) { _busqueda.value = valor }
    fun filtrar(valor: String) { _categoria.value = valor }
    fun obtener(id: Int) = _uiState.value.productos.find { it.id == id }

    // GET /productos. Si falla, muestra el catálogo local y un mensaje con opción de reintentar.
    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val lista = repository.obtenerProductos()
                _uiState.update { it.copy(isLoading = false, productos = lista) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        productos = it.productos.ifEmpty { repository.productosLocales() },
                        errorMessage = mensajeError("Sin conexión con el servidor, mostrando catálogo local", e)
                    )
                }
            }
        }
    }

    // POST /productos. Usa el id que asigna el servidor.
    fun agregarProducto(p: Producto) {
        viewModelScope.launch {
            try {
                val creado = repository.crearProducto(p)
                _uiState.update { it.copy(productos = it.productos + creado, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = mensajeError("No se pudo crear el producto", e)) }
            }
        }
    }

    // PUT /productos/{id}
    fun actualizarProducto(productoActualizado: Producto) {
        viewModelScope.launch {
            try {
                val guardado = repository.actualizarProducto(productoActualizado)
                _uiState.update { estado ->
                    estado.copy(
                        productos = estado.productos.map { if (it.id == guardado.id) guardado else it },
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = mensajeError("No se pudo actualizar el producto", e)) }
            }
        }
    }

    // DELETE /productos/{id}
    fun eliminarProducto(id: Int) {
        viewModelScope.launch {
            try {
                repository.eliminarProducto(id)
                _uiState.update { estado ->
                    estado.copy(productos = estado.productos.filter { it.id != id }, errorMessage = null)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = mensajeError("No se pudo eliminar el producto", e)) }
            }
        }
    }

    private fun mensajeError(prefijo: String, e: Exception): String =
        "$prefijo: ${e.message ?: "error desconocido"}"
}