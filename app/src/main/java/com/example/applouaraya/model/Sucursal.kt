package com.example.applouaraya.model

/**
 * Modelo de dominio para representar ferias, showrooms y puntos de exhibición en el mapa.
 */
data class Sucursal(
    val id: Int,
    val nombre: String,
    val tipo: String, // "Feria", "Showroom", "Taller"
    val direccion: String,
    val comuna: String,
    val latitud: Double,
    val longitud: Double,
    val fechaEvento: String? = null,
    val horario: String = "10:00 - 19:00",
    val activa: Boolean = true
)

/**
 * Lista de sucursales y ferias precargadas según el caso de estudio de Lou Araya Macramé
 */
object SucursalesMock {
    val lista = listOf(
        Sucursal(
            id = 1,
            nombre = "Feria Pueblo Artesanal",
            tipo = "Feria",
            direccion = "Parque Bicentenario",
            comuna = "Vitacura",
            latitud = -33.3995,
            longitud = -70.5985,
            fechaEvento = "12 - 14 Abr"
        ),
        Sucursal(
            id = 2,
            nombre = "Showroom Las Condes",
            tipo = "Showroom",
            direccion = "Av. Apoquindo 4500",
            comuna = "Las Condes",
            latitud = -33.4150,
            longitud = -70.5830,
            fechaEvento = "Permanente"
        ),
        Sucursal(
            id = 3,
            nombre = "Taller Central Lou Araya",
            tipo = "Taller",
            direccion = "Av. Italia 1200",
            comuna = "Providencia",
            latitud = -33.4420,
            longitud = -70.6270,
            fechaEvento = "Lunes a Viernes"
        ),
        Sucursal(
            id = 4,
            nombre = "Bazar Textil & Decoración",
            tipo = "Feria",
            direccion = "Plaza Ñuñoa",
            comuna = "Ñuñoa",
            latitud = -33.4560,
            longitud = -70.5980,
            fechaEvento = "Fines de semana"
        )
    )
}