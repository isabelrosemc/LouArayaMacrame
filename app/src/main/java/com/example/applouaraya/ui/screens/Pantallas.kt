package com.example.applouaraya.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.model.Producto
import com.example.applouaraya.viewmodel.*

@Composable
fun BienvenidaScreen(irLogin: () -> Unit, irRegistro: () -> Unit, irDemo: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("Lou Araya Macramé", style = MaterialTheme.typography.headlineLarge)
        Text("Diseños artesanales para eventos e interiores.")
        Spacer(Modifier.height(20.dp))
        Button(onClick = irLogin, modifier = Modifier.fillMaxWidth()) { Text("Comenzar") }
        OutlinedButton(onClick = irRegistro, modifier = Modifier.fillMaxWidth()) { Text("Crear cuenta") }
        TextButton(onClick = irDemo, modifier = Modifier.fillMaxWidth()) { Text("Ir al inicio de sesión") }
    }
}

@Composable
fun DashboardScreen(irCatalogo: () -> Unit, irFavoritos: () -> Unit,
                    irMapa: () -> Unit, irQR: () -> Unit, irSolicitudes: () -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Descubre nuestros diseños", style = MaterialTheme.typography.headlineMedium)
        listOf("Catálogo" to irCatalogo, "Mis favoritos" to irFavoritos, "Mapa de ferias" to irMapa,
            "Escáner QR" to irQR, "Solicitudes" to irSolicitudes).forEach { (texto, accion) ->
            Button(onClick = accion, modifier = Modifier.fillMaxWidth()) { Text(texto) }
        }
        Text("Las solicitudes se guardan como borradores locales; no se envían al servidor.")
    }
}

@Composable
fun ProductoCard(producto: Producto, favorito: Boolean, onClick: () -> Unit, onFavorito: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Column(Modifier.padding(16.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
            Text(producto.categoria)
            Text("$${producto.precio} CLP (referencial)")
            Text(if (producto.disponible) "Disponible" else "A pedido")
            TextButton(onClick = onFavorito) { Text(if (favorito) "♥ Quitar favorito" else "♡ Agregar favorito") }
        }
    }
}

@Composable
fun CatalogoScreen(viewModel: ProductoViewModel, favoritosViewModel: FavoritosViewModel, irDetalle: (Int) -> Unit) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val categoria by viewModel.categoria.collectAsStateWithLifecycle()
    val favoritos by favoritosViewModel.favoritos.collectAsStateWithLifecycle()
    val filtrados = productos.filter { it.nombre.contains(busqueda, ignoreCase = true) &&
        (categoria == "Todas" || it.categoria == categoria) }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(value = busqueda, onValueChange = viewModel::buscar,
            label = { Text("Buscar diseños") }, modifier = Modifier.fillMaxWidth())
        Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("Todas", "Matrimonios", "Festivales", "Interiores").forEach { opcion ->
                FilterChip(selected = categoria == opcion, onClick = { viewModel.filtrar(opcion) },
                    label = { Text(opcion, style = MaterialTheme.typography.labelSmall) })
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtrados, key = { it.id }) { p ->
                ProductoCard(p, p.id in favoritos, { irDetalle(p.id) }, { favoritosViewModel.cambiar(p.id) })
            }
        }
    }
}

@Composable
fun DetalleProductoScreen(id: Int, productos: ProductoViewModel, favoritos: FavoritosViewModel, irCotizacion: () -> Unit) {
    val producto = productos.obtener(id)
    val ids by favoritos.favoritos.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (producto == null) { Text("Producto no encontrado"); return@Column }
        Text(producto.nombre, style = MaterialTheme.typography.headlineLarge)
        Text(producto.descripcion)
        Text("Categoría: ${producto.categoria}")
        Text("Material: ${producto.material}")
        Text("Dimensiones: ${producto.dimensiones}")
        Text("Precio referencial: $${producto.precio} CLP")
        Text(if (producto.disponible) "Disponible" else "Confección a pedido")
        OutlinedButton(onClick = { favoritos.cambiar(id) }) {
            Text(if (id in ids) "Quitar favorito" else "Agregar a favoritos")
        }
        Button(onClick = irCotizacion) { Text("Solicitar cotización") }
    }
}

@Composable
fun FavoritosScreen(productos: ProductoViewModel, favoritos: FavoritosViewModel,
                    irDetalle: (Int) -> Unit, irCotizacion: (Int) -> Unit) {
    val lista by productos.productos.collectAsStateWithLifecycle()
    val ids by favoritos.favoritos.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis favoritos", style = MaterialTheme.typography.headlineLarge)
        if (ids.isEmpty()) Text("No tienes favoritos.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(lista.filter { it.id in ids }, key = { it.id }) { p ->
                ProductoCard(p, true, { irDetalle(p.id) }, { favoritos.cambiar(p.id) })
                Button(onClick = { irCotizacion(p.id) }) { Text("Cotizar ${p.nombre}") }
            }
        }
    }
}

@Composable
fun CotizacionScreen(viewModel: CotizacionViewModel) {
    val datos by viewModel.datos.collectAsStateWithLifecycle()
    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Solicitar cotización", style = MaterialTheme.typography.headlineLarge)
        @Composable fun Campo(valor: String, cambio: (String) -> Unit, etiqueta: String) {
            OutlinedTextField(valor, cambio, label = { Text(etiqueta) }, modifier = Modifier.fillMaxWidth())
        }
        Campo(datos.evento, { viewModel.actualizar(datos.copy(evento = it)) }, "Tipo de evento")
        Campo(datos.medidas, { viewModel.actualizar(datos.copy(medidas = it)) }, "Medidas")
        Campo(datos.material, { viewModel.actualizar(datos.copy(material = it)) }, "Material")
        Campo(datos.colores, { viewModel.actualizar(datos.copy(colores = it)) }, "Colores")
        Campo(datos.fecha, { viewModel.actualizar(datos.copy(fecha = it)) }, "Fecha AAAA-MM-DD")
        Campo(datos.observaciones, { viewModel.actualizar(datos.copy(observaciones = it)) }, "Observaciones")
        Button(onClick = viewModel::guardar, modifier = Modifier.fillMaxWidth()) { Text("Guardar borrador de prueba") }
        AnimatedVisibility(visible = mensaje.startsWith("Borrador guardado"), enter = fadeIn() + scaleIn()) { Text(mensaje) }
        if (mensaje.isNotEmpty() && !mensaje.startsWith("Borrador guardado")) Text(mensaje)
    }
}

@Composable
fun SolicitudesScreen(viewModel: CotizacionViewModel, irCotizacion: () -> Unit) {
    val borradores by viewModel.borradores.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis solicitudes", style = MaterialTheme.typography.headlineLarge)
        Button(onClick = irCotizacion) { Text("Nueva solicitud") }
        if (borradores.isEmpty()) Text("No hay borradores guardados.")
        LazyColumn {
            items(borradores) { b ->
                Text("${b.tipoEvento} · ${b.fecha} · ${b.estado}",
                    modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

@Composable
fun PantallaPendiente(titulo: String, descripcion: String) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text(descripcion)
    }
}
