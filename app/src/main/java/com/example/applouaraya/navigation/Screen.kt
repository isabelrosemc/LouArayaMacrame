package com.example.applouaraya.navigation

sealed class Screen(val route: String) {
    object Bienvenida: Screen("bienvenida")
    object Registro: Screen("registro")
    object Login: Screen("login")
    object Dashboard: Screen("dashboard")
    object Catalogo: Screen("catalogo")
    object DetalleProducto: Screen("detalle/{productoId}") {
        fun crearRuta(id: Int) = "detalle/$id"
    }
    object Favoritos: Screen("favoritos")
    object Cotizacion: Screen("cotizacion/{productoId}") {
        fun crearRuta(id: Int = -1) = "cotizacion/$id"
    }
    object Mapa: Screen("mapa")
    object EscanerQR: Screen("escaner_qr")
    object Solicitudes: Screen("solicitudes")
    object Perfil: Screen("perfil")
    object GestionProductos: Screen("gestion_productos")
    object GestionCampanas: Screen("gestion_campanas")
    object Notificaciones: Screen("notificaciones")
}
