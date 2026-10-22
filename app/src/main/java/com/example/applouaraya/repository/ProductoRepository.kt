package com.example.applouaraya.repository

import com.example.applouaraya.model.Producto

class ProductoRepository {
    fun obtenerProductos(): List<Producto> = listOf(
        Producto(1, "Arco de Macramé", "Matrimonios", "Arco artesanal para ceremonias.", "Algodón y madera", "200 x 180 cm", 120000, true),
        Producto(2, "Colgante Festival", "Festivales", "Decoración artesanal para eventos.", "Cordón de algodón", "80 x 50 cm", 45000, true),
        Producto(3, "Tapiz Bohemio", "Interiores", "Tapiz para decoración del hogar.", "Algodón natural", "120 x 80 cm", 65000, false)
    )
}
