package com.example.applouaraya.data.remote.dto

// Modelo propuesto; debe adaptarse al contrato JSON real del servidor.
data class ProductoDto(
    val id: Int, val nombre: String, val descripcion: String, val categoria: String,
    val material: String, val dimensiones: String, val precio: Int, val disponible: Boolean
)
