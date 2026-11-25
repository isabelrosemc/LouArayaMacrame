package com.example.applouaraya.repository

import com.example.applouaraya.data.remote.ApiService
import com.example.applouaraya.data.remote.RetrofitInstance
import com.example.applouaraya.data.remote.dto.toDto
import com.example.applouaraya.data.remote.dto.toModel
import com.example.applouaraya.model.Producto
import retrofit2.HttpException

// Única puerta de acceso a los productos: API REST + catálogo local de respaldo.
class ProductoRepository {
    // get(): si la URL base fuera inválida, el error ocurre dentro del try del ViewModel (no al crearlo)
    private val api: ApiService get() = RetrofitInstance.api

    suspend fun obtenerProductos(): List<Producto> = api.getProductos().map { it.toModel() }

    suspend fun obtenerProducto(id: Int): Producto = api.getProducto(id).toModel()

    suspend fun crearProducto(p: Producto): Producto = api.createProducto(p.toDto()).toModel()

    suspend fun actualizarProducto(p: Producto): Producto = api.updateProducto(p.id, p.toDto()).toModel()

    suspend fun eliminarProducto(id: Int) {
        val respuesta = api.deleteProducto(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    // Para validar el QR: consulta la API y, si no hay red, el catálogo local
    suspend fun existeProducto(id: Int): Boolean =
        try { obtenerProductos().any { it.id == id } }
        catch (_: Exception) { productosLocales().any { it.id == id } }

    // Respaldo cuando no hay conexión o la API no responde
    fun productosLocales(): List<Producto> = listOf(
        Producto(1, "Arco de Macramé", "Matrimonios", "Arco artesanal para ceremonias.", "Algodón y madera", "200 x 180 cm", 120000, true),
        Producto(2, "Colgante Festival", "Festivales", "Decoración artesanal para eventos.", "Cordón de algodón", "80 x 50 cm", 45000, true),
        Producto(3, "Tapiz Bohemio", "Interiores", "Tapiz para decoración del hogar.", "Algodón natural", "120 x 80 cm", 65000, false)
    )
}
