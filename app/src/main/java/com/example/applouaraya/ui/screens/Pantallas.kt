package com.example.applouaraya.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.R
import com.example.applouaraya.model.Producto
import com.example.applouaraya.viewmodel.*

// Colores del Mockup Lou Araya
private val Terracota = Color(0xFFB85D38)
private val FondoLino = Color(0xFFFBF7F2)
private val ContenedorBeige = Color(0xFFF3ECE2)
private val BordeSuave = Color(0xFFE2D6C8)
private val TextoTierra = Color(0xFF2D231E)
private val TextoSecundario = Color(0xFF7A6E65)

// Función para asociar cada pieza a su fotografía en res/drawable
fun obtenerImagenProducto(nombre: String): Int {
    val n = nombre.lowercase()
    return when {
        n.contains("arco") -> R.drawable.arco_macrame
        n.contains("colgante") || n.contains("festival") -> R.drawable.colgante_festival
        n.contains("tapiz") || n.contains("bohemio") -> R.drawable.tapiz_bohemio
        else -> R.drawable.arco_macrame
    }
}

@Composable
fun BienvenidaScreen(irLogin: () -> Unit, irRegistro: () -> Unit, irDemo: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "✿",
                fontSize = 32.sp,
                color = Terracota
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Lou Araya Macramé",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextoTierra
            )
            Text(
                text = "Arte en fibras que decora tus historias",
                fontSize = 14.sp,
                color = TextoSecundario
            )

            Spacer(Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(ContenedorBeige),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tapiz_bohemio),
                    contentDescription = "Taller Lou Araya",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Taller Textil & Deco Handmade",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            val propuestas = listOf(
                "Diseños artesanales únicos",
                "Para hogares, eventos y más",
                "Encuentra en ferias y showrooms",
                "Solicita diseños personalizados"
            )
            propuestas.forEach { texto ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ContenedorBeige)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(Terracota)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = texto,
                        color = TextoTierra,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = irLogin,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Comenzar", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            }

            OutlinedButton(
                onClick = irRegistro,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Terracota),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Terracota),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Ya tengo cuenta", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }

            TextButton(
                onClick = irDemo,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Explorar modo demo", fontSize = 13.sp, color = TextoSecundario)
            }
        }
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
            .background(FondoLino)
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Descubre nuestros diseños", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = TextoTierra)
        Text("Explora colecciones textiles hechas a mano con fibras naturales.", color = TextoSecundario, fontSize = 13.sp)

        val accesos = listOf(
            Triple("Catálogo completo", Icons.Default.Search, irCatalogo),
            Triple("Mis favoritos", Icons.Default.Favorite, irFavoritos),
            Triple("Mapa de ferias", Icons.Default.Place, irMapa),
            Triple("Escanear código QR", Icons.Default.Search, irQR),
            Triple("Solicitudes y compras", Icons.Default.Edit, irSolicitudes)
        )

        accesos.forEach { (titulo, icono, accion) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = accion),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(icono, contentDescription = null, tint = Terracota)
                        }
                        Spacer(Modifier.width(14.dp))
                        Text(titulo, fontWeight = FontWeight.SemiBold, color = TextoTierra, fontSize = 15.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextoSecundario)
                }
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
                (categoria == "Todas" || categoria == "Todos" || it.categoria == categoria)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = viewModel::buscar,
            placeholder = { Text("Buscar diseños...", color = TextoSecundario, fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextoSecundario) },
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = ContenedorBeige,
                unfocusedContainerColor = ContenedorBeige,
                focusedBorderColor = Terracota,
                unfocusedBorderColor = Color.Transparent
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(14.dp))
        Text("Catálogo", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
        Spacer(Modifier.height(8.dp))

        val categoriasFiltro = listOf("Todos", "Matrimonios", "Festivales", "Interiores")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoriasFiltro.forEach { cat ->
                val activa = (categoria == cat) || (cat == "Todos" && categoria == "Todas")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (activa) Terracota else Color.Transparent,
                    border = if (activa) null else BorderStroke(1.dp, BordeSuave),
                    modifier = Modifier.clickable {
                        viewModel.filtrar(if (cat == "Todos") "Todas" else cat)
                    }
                ) {
                    Text(
                        text = cat,
                        color = if (activa) Color.White else TextoTierra,
                        fontSize = 13.sp,
                        fontWeight = if (activa) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filtrados, key = { it.id }) { p ->
                val esFavorito = p.id in favoritos
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { irDetalle(p.id) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                                .background(Color(0xFFE9DFD3))
                        ) {
                            Image(
                                painter = painterResource(id = obtenerImagenProducto(p.nombre)),
                                contentDescription = p.nombre,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f))
                                    .clickable { favoritosViewModel.cambiar(p.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Favorito",
                                    tint = if (esFavorito) Color.Red else Color.LightGray,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = p.nombre,
                                fontWeight = FontWeight.SemiBold,
                                color = TextoTierra,
                                fontSize = 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "$${p.precio} CLP",
                                fontWeight = FontWeight.Bold,
                                color = Terracota,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
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
            .background(FondoLino)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (producto == null) {
            Text("Producto no encontrado", color = TextoTierra)
            return@Column
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(ContenedorBeige)
        ) {
            Image(
                painter = painterResource(id = obtenerImagenProducto(producto.nombre)),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(producto.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
        Text(producto.descripcion, color = TextoSecundario, fontSize = 14.sp)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
        ) {
            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Categoría: ${producto.categoria}", color = TextoTierra, fontSize = 13.sp)
                Text("Material: ${producto.material}", color = TextoTierra, fontSize = 13.sp)
                Text("Dimensiones: ${producto.dimensiones}", color = TextoTierra, fontSize = 13.sp)
                Text("Precio referencial: $${producto.precio} CLP", fontWeight = FontWeight.Bold, color = Terracota)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = { favoritos.cambiar(id) },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Terracota),
                modifier = Modifier.weight(1f).height(48.dp)
            ) {
                Text(if (id in ids) "Quitar ♥" else "Favorito ♡", color = Terracota)
            }
            Button(
                onClick = irCotizacion,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                modifier = Modifier.weight(1.3f).height(48.dp)
            ) {
                Text("Cotizar diseño", color = Color.White)
            }
        }
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
    val favs = lista.filter { it.id in ids }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(16.dp)
    ) {
        Text("Mis favoritos", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
        Spacer(Modifier.height(12.dp))

        if (favs.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes favoritos guardados.", color = TextoSecundario)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(favs, key = { it.id }) { p ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { irDetalle(p.id) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Image(
                                    painter = painterResource(id = obtenerImagenProducto(p.nombre)),
                                    contentDescription = p.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(p.nombre, fontWeight = FontWeight.Bold, color = TextoTierra)
                                    Text("$${p.precio} CLP", color = Terracota, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            IconButton(onClick = { irCotizacion(p.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Cotizar", tint = Terracota)
                            }
                            IconButton(onClick = { favoritos.cambiar(p.id) }) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                            }
                        }
                    }
                }
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
            .background(FondoLino)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Solicitud de Personalización", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
        Text("Cuéntanos qué diseño buscas y te responderemos con presupuesto.", color = TextoSecundario, fontSize = 13.sp)

        OutlinedTextField(
            value = datos.evento,
            onValueChange = { valor -> viewModel.actualizar { it.copy(evento = valor) } },
            label = { Text("Tipo de evento (Matrimonio, Hogar...)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.medidas,
            onValueChange = { valor -> viewModel.actualizar { it.copy(medidas = valor) } },
            label = { Text("Dimensiones aproximadas (ej: 2x1.5 m)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.material,
            onValueChange = { valor -> viewModel.actualizar { it.copy(material = valor) } },
            label = { Text("Material preferido (Algodón, Yute...)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.colores,
            onValueChange = { valor -> viewModel.actualizar { it.copy(colores = valor) } },
            label = { Text("Color o paleta de colores") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.fecha,
            onValueChange = { valor -> viewModel.actualizar { it.copy(fecha = valor) } },
            label = { Text("Fecha requerida (AAAA-MM-DD)") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = datos.observaciones,
            onValueChange = { valor -> viewModel.actualizar { it.copy(observaciones = valor) } },
            label = { Text("Observaciones / Referencias") },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { viewModel.guardar() },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Terracota),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Enviar solicitud", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        }

        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = Terracota, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun SolicitudesScreen(
    viewModel: CotizacionViewModel,
    irCotizacion: () -> Unit = {}
) {
    val borradores by viewModel.borradores.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis solicitudes", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
            Button(
                onClick = irCotizacion,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Terracota)
            ) { Text("Nueva") }
        }

        Spacer(Modifier.height(14.dp))

        if (borradores.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay solicitudes registradas.", color = TextoSecundario)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(borradores, key = { it.id }) { b ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Solicitud #${b.id}", fontWeight = FontWeight.Bold, color = TextoTierra)
                                Surface(
                                    color = Terracota.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        b.estado,
                                        color = Terracota,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Evento: ${b.tipoEvento} · Fecha: ${b.fecha}", fontSize = 13.sp, color = TextoSecundario)
                            Text("Medidas: ${b.medidas} · Material: ${b.material}", fontSize = 13.sp, color = TextoSecundario)
                        }
                    }
                }
            }
        }
    }
}

// Pantallas del Rol Administrador
@Composable
fun AdminSolicitudesScreen(vmCotizacion: CotizacionViewModel) {
    val solicitudes by vmCotizacion.solicitudes.collectAsStateWithLifecycle()
    val mensaje by vmCotizacion.mensaje.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(16.dp)
    ) {
        Text("Panel Admin: Solicitudes", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
        Text("Aprobación y revisión de pedidos", fontSize = 13.sp, color = TextoSecundario)

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(mensaje, color = Terracota, fontSize = 13.sp)
        }

        Spacer(Modifier.height(14.dp))

        if (solicitudes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay solicitudes pendientes.", color = TextoSecundario)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(solicitudes, key = { it.id }) { s ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Solicitud #${s.id} (Cliente: ${s.clienteId})", fontWeight = FontWeight.Bold, color = TextoTierra)
                                Surface(
                                    color = when (s.estado) {
                                        "Aprobada" -> Color(0xFF2E7D32)
                                        "Rechazada" -> MaterialTheme.colorScheme.error
                                        else -> Terracota
                                    },
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        s.estado,
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Evento: ${s.tipoEvento} | Medidas: ${s.medidas}", fontSize = 12.sp, color = TextoSecundario)
                            Text("Material: ${s.material} | Color: ${s.colores}", fontSize = 12.sp, color = TextoSecundario)

                            Spacer(Modifier.height(10.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                OutlinedButton(
                                    onClick = { vmCotizacion.cambiarEstado(s.id, "Rechazada") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                    modifier = Modifier.height(36.dp)
                                ) { Text("Rechazar", fontSize = 12.sp) }

                                Spacer(Modifier.width(8.dp))

                                Button(
                                    onClick = { vmCotizacion.cambiarEstado(s.id, "Aprobada") },
                                    colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                                    modifier = Modifier.height(36.dp)
                                ) { Text("Aprobar", fontSize = 12.sp, color = Color.White) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminGestionProductosScreen(vmProductos: ProductoViewModel) {
    var mostrarFormulario by remember { mutableStateOf(false) }
    var productoIdEdicion by remember { mutableStateOf<Int?>(null) }

    var nombre by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Matrimonios") }
    var precioTexto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var dimensiones by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("Algodón natural") }

    val productos by vmProductos.productos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLino)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Gestión de Productos", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoTierra)
            Button(
                onClick = {
                    if (mostrarFormulario) {
                        mostrarFormulario = false
                        productoIdEdicion = null
                    } else {
                        nombre = ""
                        categoria = "Matrimonios"
                        precioTexto = ""
                        descripcion = ""
                        dimensiones = ""
                        material = "Algodón natural"
                        productoIdEdicion = null
                        mostrarFormulario = true
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(if (mostrarFormulario) "Cerrar" else "+ Nuevo", color = Color.White)
            }
        }

        Spacer(Modifier.height(10.dp))

        if (mostrarFormulario) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        if (productoIdEdicion == null) "Crear Nuevo Producto" else "Editar Producto #$productoIdEdicion",
                        fontWeight = FontWeight.Bold,
                        color = TextoTierra
                    )
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoría") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = precioTexto, onValueChange = { precioTexto = it }, label = { Text("Precio CLP") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = material, onValueChange = { material = it }, label = { Text("Material") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = dimensiones, onValueChange = { dimensiones = it }, label = { Text("Dimensiones") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())

                    Button(
                        onClick = {
                            val precio = precioTexto.toIntOrNull()
                            if (nombre.isNotBlank() && precio != null) {
                                if (productoIdEdicion == null) {
                                    val nuevoId = (productos.maxOfOrNull { it.id } ?: 0) + 1
                                    vmProductos.agregarProducto(
                                        Producto(
                                            id = nuevoId,
                                            nombre = nombre.trim(),
                                            categoria = categoria.trim(),
                                            precio = precio,
                                            descripcion = descripcion.trim().ifBlank { "Pieza artesanal" },
                                            material = material.trim(),
                                            dimensiones = dimensiones.trim().ifBlank { "Estándar" },
                                            disponible = true
                                        )
                                    )
                                } else {
                                    vmProductos.actualizarProducto(
                                        Producto(
                                            id = productoIdEdicion!!,
                                            nombre = nombre.trim(),
                                            categoria = categoria.trim(),
                                            precio = precio,
                                            descripcion = descripcion.trim().ifBlank { "Pieza artesanal" },
                                            material = material.trim(),
                                            dimensiones = dimensiones.trim().ifBlank { "Estándar" },
                                            disponible = true
                                        )
                                    )
                                }
                                nombre = ""
                                precioTexto = ""
                                productoIdEdicion = null
                                mostrarFormulario = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Terracota),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (productoIdEdicion == null) "Publicar" else "Guardar Cambios", color = Color.White)
                    }
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(productos, key = { it.id }) { p ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = ContenedorBeige)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(p.nombre, fontWeight = FontWeight.Bold, color = TextoTierra)
                            Text("${p.categoria} · $${p.precio} CLP", fontSize = 12.sp, color = Terracota)
                        }

                        // Botón de edición para modificar cualquier producto (incluidos los 3 base)
                        IconButton(onClick = {
                            productoIdEdicion = p.id
                            nombre = p.nombre
                            categoria = p.categoria
                            precioTexto = p.precio.toString()
                            material = p.material
                            dimensiones = p.dimensiones
                            descripcion = p.descripcion
                            mostrarFormulario = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Terracota)
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
            .background(FondoLino)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium, color = TextoTierra)
        Spacer(Modifier.height(8.dp))
        Text(descripcion, color = TextoSecundario)
    }
}