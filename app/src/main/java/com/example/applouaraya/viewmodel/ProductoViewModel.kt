package com.example.applouaraya.viewmodel

import androidx.lifecycle.ViewModel
import com.example.applouaraya.model.Producto
import com.example.applouaraya.repository.ProductoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProductoViewModel : ViewModel() {
    private val repository = ProductoRepository()
    private val _productos = MutableStateFlow(repository.obtenerProductos())
    val productos = _productos.asStateFlow()
    private val _busqueda = MutableStateFlow("")
    val busqueda = _busqueda.asStateFlow()
    private val _categoria = MutableStateFlow("Todas")
    val categoria = _categoria.asStateFlow()

    fun buscar(valor: String) { _busqueda.value = valor }
    fun filtrar(valor: String) { _categoria.value = valor }
    fun obtener(id: Int) = _productos.value.find { it.id == id }

    // Agrega una nueva pieza textil
    fun agregarProducto(p: Producto) {
        _productos.value = _productos.value + p
    }

    // Permite actualizar piezas existentes (precio, medidas, etc.) sin romper dependencias
    fun actualizarProducto(productoActualizado: Producto) {
        _productos.value = _productos.value.map { producto ->
            if (producto.id == productoActualizado.id) productoActualizado else producto
        }
    }
}