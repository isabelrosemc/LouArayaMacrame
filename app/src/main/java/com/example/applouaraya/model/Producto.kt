package com.example.applouaraya.model

data class Producto(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val material: String,
    val dimensiones: String,
    val precio: Int,
    val disponible: Boolean
)
