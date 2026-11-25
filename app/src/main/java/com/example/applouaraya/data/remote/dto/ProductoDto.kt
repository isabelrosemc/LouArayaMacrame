package com.example.applouaraya.data.remote.dto

import com.example.applouaraya.model.Producto

// Forma exacta del JSON que entrega la API (los nombres deben coincidir con las claves).
data class ProductoDto(
    val id: Int, val nombre: String, val descripcion: String, val categoria: String,
    val material: String, val dimensiones: String, val precio: Int, val disponible: Boolean
)

// DTO (red) -> modelo que usa la app
fun ProductoDto.toModel() = Producto(
    id = id, nombre = nombre, categoria = categoria, descripcion = descripcion,
    material = material, dimensiones = dimensiones, precio = precio, disponible = disponible
)

// Modelo de la app -> DTO (para enviar con POST / PUT)
fun Producto.toDto() = ProductoDto(
    id = id, nombre = nombre, descripcion = descripcion, categoria = categoria,
    material = material, dimensiones = dimensiones, precio = precio, disponible = disponible
)
