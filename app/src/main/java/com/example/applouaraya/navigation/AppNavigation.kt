package com.example.applouaraya.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.applouaraya.AppContainer
import com.example.applouaraya.ui.screens.*
import com.example.applouaraya.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val container = remember(context.applicationContext) { AppContainer(context.applicationContext) }
    val nav = rememberNavController()
    val sesion: SesionViewModel = viewModel()
    val productos: ProductoViewModel = viewModel()
    val favoritos: FavoritosViewModel = viewModel()
    val cotizacion: CotizacionViewModel = viewModel()
    val qr: EscanerQRViewModel = viewModel()
    val clienteId by sesion.clienteId.collectAsStateWithLifecycle()
    val entry by nav.currentBackStackEntryAsState()
    val ruta = entry?.destination?.route
    val publico = ruta in listOf(Screen.Bienvenida.route, Screen.Login.route, Screen.Registro.route)

    LaunchedEffect(container) {
        sesion.configurar(container.local)
        favoritos.configurar(container.local)
        cotizacion.configurar(container.local)
        qr.configurar(container.local)
    }
    LaunchedEffect(clienteId) {
        favoritos.seleccionar(clienteId)
        cotizacion.seleccionar(clienteId)
        if (clienteId != null) {
            nav.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Bienvenida.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
    Scaffold(
        topBar = { if (!publico && clienteId != null) TopAppBar(title = { Text("Lou Araya Macramé") }) },
        bottomBar = {
            if (!publico && clienteId != null) NavigationBar {
                listOf(Screen.Dashboard to "Inicio", Screen.Catalogo to "Catálogo",
                    Screen.Favoritos to "Favoritos", Screen.Solicitudes to "Solicitudes",
                    Screen.Perfil to "Perfil").forEach { (pantalla, nombre) ->
                    NavigationBarItem(selected = ruta == pantalla.route,
                        onClick = { nav.navigate(pantalla.route) { launchSingleTop = true } },
                        icon = { Text("•") }, label = { Text(nombre) })
                }
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = Screen.Bienvenida.route,
            modifier = Modifier.padding(padding)) {
            composable(Screen.Bienvenida.route) {
                BienvenidaScreen(irLogin = { nav.navigate(Screen.Login.route) },
                    irRegistro = { nav.navigate(Screen.Registro.route) },
                    irDemo = { nav.navigate(Screen.Login.route) })
            }
            composable(Screen.Login.route) {
                LoginScreen(sesion, irRegistro = { nav.navigate(Screen.Registro.route) })
            }
            composable(Screen.Registro.route) {
                RegistroScreen(sesion, volver = { nav.navigate(Screen.Login.route) })
            }
            composable(Screen.Dashboard.route) {
                if (clienteId == null) PantallaPendiente("Sesión requerida", "Inicia sesión.")
                else DashboardScreen(irCatalogo = { nav.navigate(Screen.Catalogo.route) },
                    irFavoritos = { nav.navigate(Screen.Favoritos.route) },
                    irMapa = { nav.navigate(Screen.Mapa.route) },
                    irQR = { nav.navigate(Screen.EscanerQR.route) },
                    irSolicitudes = { nav.navigate(Screen.Solicitudes.route) })
            }
            composable(Screen.Catalogo.route) {
                if (clienteId != null) CatalogoScreen(productos, favoritos,
                    irDetalle = { nav.navigate(Screen.DetalleProducto.crearRuta(it)) })
            }
            composable(Screen.DetalleProducto.route,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })) { back ->
                if (clienteId != null) {
                    val id = back.arguments?.getInt("productoId") ?: -1
                    DetalleProductoScreen(id, productos, favoritos,
                        irCotizacion = { nav.navigate(Screen.Cotizacion.crearRuta(id)) })
                }
            }
            composable(Screen.Favoritos.route) {
                if (clienteId != null) FavoritosScreen(productos, favoritos,
                    irDetalle = { nav.navigate(Screen.DetalleProducto.crearRuta(it)) },
                    irCotizacion = { nav.navigate(Screen.Cotizacion.crearRuta(it)) })
            }
            composable(Screen.Cotizacion.route,
                arguments = listOf(navArgument("productoId") { type = NavType.IntType })) { back ->
                if (clienteId != null) {
                    val id = back.arguments?.getInt("productoId") ?: -1
                    LaunchedEffect(id) { cotizacion.preparar(if (id < 0) null else id) }
                    CotizacionScreen(cotizacion)
                }
            }
            composable(Screen.Solicitudes.route) {
                if (clienteId != null) SolicitudesScreen(cotizacion,
                    irCotizacion = { nav.navigate(Screen.Cotizacion.crearRuta()) })
            }
            composable(Screen.Mapa.route) { if (clienteId != null) MapaScreen() }
            composable(Screen.EscanerQR.route) {
                if (clienteId != null) EscanerQRScreen(qr, clienteId,
                    irDetalle = { nav.navigate(Screen.DetalleProducto.crearRuta(it)) })
            }
            composable(Screen.Perfil.route) {
                if (clienteId != null) PerfilScreen(
                    clienteId = clienteId!!,
                    irSalir = {
                        sesion.salir()
                        nav.navigate(Screen.Login.route) {
                            popUpTo(Screen.Dashboard.route) { inclusive = true }
                        }
                    },
                    irNotificaciones = { nav.navigate(Screen.Notificaciones.route) }
                )
            }
            // Las pantallas administrativas NO se habilitan para clientes.
            composable(Screen.GestionProductos.route) {
                PantallaPendiente("Acceso restringido", "Falta autenticación administrativa del backend.")
            }
            composable(Screen.GestionCampanas.route) {
                PantallaPendiente("Acceso restringido", "Falta autenticación administrativa del backend.")
            }
            composable(Screen.Notificaciones.route) {
                PantallaPendiente("Notificaciones", "Firebase y preferencias aún no configuradas.")
            }
        }
    }
}
