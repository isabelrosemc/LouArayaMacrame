package com.example.applouaraya.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.model.Producto
import com.example.applouaraya.viewmodel.*

@Composable
fun BienvenidaScreen(irLogin: () -> Unit, irRegistro: () -> Unit, irDemo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Lou Araya Macramé", style = MaterialTheme.typography.headlineLarge)
        Text("Diseños artesanales para eventos e interiores.")
        Spacer(Modifier.height(20.dp))
        Button(onClick = irLogin, modifier = Modifier.fillMaxWidth()) { Text("Comenzar") }
        OutlinedButton(onClick = irRegistro, modifier = Modifier.fillMaxWidth()) { Text("Crear cuenta") }
        TextButton(onClick = irDemo, modifier = Modifier.fillMaxWidth()) { Text("Ir al inicio de sesión") }
    }
}

@Composable
fun DashboardScreen(
    irCatalogo: () -> Unit,
    irFavoritos: () -> Unit,
    irMapa: () -> Unit,
    irQR: () -> Unit,
    irSolicitudes: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Descubre nuestros diseños", style = MaterialTheme.typography.headlineMedium)
        listOf(
            "Catálogo" to irCatalogo,
            "Mis favoritos" to irFavoritos,
            "Mapa de ferias" to irMapa,
            "Escáner QR" to irQR,
            "Solicitudes" to irSolicitudes
        ).forEach { (texto, accion) ->
            Button(onClick = accion, modifier = Modifier.fillMaxWidth()) { Text(texto) }
        }
        Text("Las solicitudes se gestionan localmente en tu dispositivo.")
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    favorito: Boolean,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(producto.nombre, style = MaterialTheme.typography.titleLarge)
            Text(producto.categoria)
            Text("$${producto.precio} CLP (referencial)")
            Text(if (producto.disponible) "Disponible" else "A pedido")
            TextButton(onClick = onFavorito) {
                Text(if (favorito) "♥ Quitar favorito" else "♡ Agregar favorito")
            }
        }
    }
}

@Composable
fun CatalogoScreen(
    viewModel: ProductoViewModel,
    favoritosViewModel: FavoritosViewModel,
    irDetalle: (Int) -> Unit
) {
    val productos by viewModel.productos.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val categoria by viewModel.categoria.collectAsStateWithLifecycle()
    val favoritos by favoritosViewModel.favoritos.collectAsStateWithLifecycle()

    val filtrados = productos.filter {
        it.nombre.contains(busqueda, ignoreCase = true) &&
                (categoria == "Todas" || it.categoria == categoria)
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(
            value = busqueda,
            onValueChange = viewModel::buscar,
            label = { Text("Buscar diseños") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Todas", "Matrimonios", "Festivales", "Interiores").forEach { opcion ->
                FilterChip(
                    selected = categoria == opcion,
                    onClick = { viewModel.filtrar(opcion) },
                    label = { Text(opcion, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtrados, key = { it.id }) { p ->
                ProductoCard(
                    producto = p,
                    favorito = p.id in favoritos,
                    onClick = { irDetalle(p.id) },
                    onFavorito = { favoritosViewModel.cambiar(p.id) }
                )
            }
        }
    }
}

@Composable
fun DetalleProductoScreen(
    id: Int,
    productos: ProductoViewModel,
    favoritos: FavoritosViewModel,
    irCotizacion: () -> Unit
) {
    val producto = productos.obtener(id)
    val ids by favoritos.favoritos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (producto == null) {
            Text("Producto no encontrado")
            return@Column
        }
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
fun FavoritosScreen(
    productos: ProductoViewModel,
    favoritos: FavoritosViewModel,
    irDetalle: (Int) -> Unit,
    irCotizacion: (Int) -> Unit
) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Solicitar cotización", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = datos.evento,
            onValueChange = { valor -> viewModel.actualizar { it.copy(evento = valor) } },
            label = { Text("Tipo de evento") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.medidas,
            onValueChange = { valor -> viewModel.actualizar { it.copy(medidas = valor) } },
            label = { Text("Medidas") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.material,
            onValueChange = { valor -> viewModel.actualizar { it.copy(material = valor) } },
            label = { Text("Material") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.colores,
            onValueChange = { valor -> viewModel.actualizar { it.copy(colores = valor) } },
            label = { Text("Colores") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.fecha,
            onValueChange = { valor -> viewModel.actualizar { it.copy(fecha = valor) } },
            label = { Text("Fecha AAAA-MM-DD") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.observaciones,
            onValueChange = { valor -> viewModel.actualizar { it.copy(observaciones = valor) } },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.guardar() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar solicitud")
        }

        AnimatedVisibility(
            visible = mensaje.contains("éxito", ignoreCase = true) || mensaje.contains("guardad", ignoreCase = true),
            enter = fadeIn() + scaleIn()
        ) {
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }

        if (mensaje.isNotEmpty() && !mensaje.contains("éxito", ignoreCase = true)) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun SolicitudesScreen(
    viewModel: CotizacionViewModel,
    irCotizacion: () -> Unit = {}
) {
    val borradores by viewModel.borradores.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis solicitudes", style = MaterialTheme.typography.headlineLarge)
            Button(onClick = irCotizacion) { Text("Nueva") }
        }

        Spacer(Modifier.height(12.dp))

        if (borradores.isEmpty()) {
            Text("No hay solicitudes registradas.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(borradores, key = { it.id }) { b ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Solicitud #${b.id} - ${b.tipoEvento}", fontWeight = FontWeight.Bold)
                            Text("Fecha: ${b.fecha} · Estado: ${b.estado}", style = MaterialTheme.typography.bodySmall)
                            Text("Material: ${b.material} · Medidas: ${b.medidas}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSolicitudesScreen(
    vmCotizacion: CotizacionViewModel
) {
    val solicitudes by vmCotizacion.solicitudes.collectAsStateWithLifecycle()
    val mensaje by vmCotizacion.mensaje.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Panel Administrador: Solicitudes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Aprobación y revisión de solicitudes de personalización",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                mensaje,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(14.dp))

        if (solicitudes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay solicitudes pendientes registradas.")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(solicitudes, key = { it.id }) { s ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Solicitud #${s.id} (Cliente: ${s.clienteId})",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Surface(
                                    color = when (s.estado) {
                                        "Aprobada" -> MaterialTheme.colorScheme.primary
                                        "Rechazada" -> MaterialTheme.colorScheme.error
                                        else -> MaterialTheme.colorScheme.outline
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = s.estado,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(6.dp))
                            Text("Evento: ${s.tipoEvento} | Medidas: ${s.medidas}")
                            Text("Material: ${s.material} | Color: ${s.colores}")
                            if (s.observaciones.isNotBlank()) {
                                Text("Observaciones: ${s.observaciones}", style = MaterialTheme.typography.bodySmall)
                            }

                            Spacer(Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { vmCotizacion.cambiarEstado(s.id, "Rechazada") },
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error
                                    )
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Rechazar")
                                }
                                Spacer(Modifier.width(8.dp))
                                Button(
                                    onClick = { vmCotizacion.cambiarEstado(s.id, "Aprobada") }
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null)
                                    Spacer(Modifier.width(4.dp))
                                    Text("Aprobar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminGestionProductosScreen(
    vmProductos: ProductoViewModel
) {
    var mostrarFormulario by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Matrimonios") }
    var precioTexto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var dimensiones by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("Algodón natural") }

    var mensajeLocal by remember { mutableStateOf<String?>(null) }
    val productos by vmProductos.productos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gestión de Productos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Catálogo y creación de piezas",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Button(onClick = { mostrarFormulario = !mostrarFormulario }) {
                Icon(
                    if (mostrarFormulario) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(if (mostrarFormulario) "Cerrar" else "Nuevo")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (mostrarFormulario) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(14.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Crear Nuevo Producto", fontWeight = FontWeight.Bold)

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it; mensajeLocal = null },
                        label = { Text("Nombre del producto *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = categoria,
                        onValueChange = { categoria = it },
                        label = { Text("Categoría (Matrimonios, Festivales, Interiores)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = precioTexto,
                        onValueChange = { precioTexto = it },
                        label = { Text("Precio en CLP *") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = dimensiones,
                        onValueChange = { dimensiones = it },
                        label = { Text("Dimensiones (ej: 2.0 x 1.5 m)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = material,
                        onValueChange = { material = it },
                        label = { Text("Material") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (mensajeLocal != null) {
                        Text(
                            mensajeLocal!!,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            val precio = precioTexto.toIntOrNull()
                            if (nombre.isBlank() || precio == null) {
                                mensajeLocal = "Nombre y precio numérico son obligatorios"
                            } else {
                                val nuevoId = (productos.maxOfOrNull { it.id } ?: 0) + 1
                                val nuevo = Producto(
                                    id = nuevoId,
                                    nombre = nombre.trim(),
                                    categoria = categoria.trim(),
                                    precio = precio,
                                    descripcion = descripcion.trim().ifBlank { "Pieza artesanal única hecha a mano." },
                                    material = material.trim(),
                                    dimensiones = dimensiones.trim().ifBlank { "Estándar" },
                                    disponible = true
                                )
                                vmProductos.agregarProducto(nuevo)
                                mensajeLocal = "Producto '${nuevo.nombre}' publicado con éxito"
                                nombre = ""
                                precioTexto = ""
                                descripcion = ""
                                mostrarFormulario = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Publicar Producto")
                    }
                }
            }
        }

        Text("Productos en catálogo:", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos, key = { it.id }) { p ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nombre, fontWeight = FontWeight.Bold)
                            Text("${p.categoria} - $${p.precio}", style = MaterialTheme.typography.bodySmall)
                            Text("Material: ${p.material}", style = MaterialTheme.typography.bodySmall)
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                if (p.disponible) "Disponible" else "Agotado",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PantallaPendiente(titulo: String, descripcion: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text(descripcion)
    }
}