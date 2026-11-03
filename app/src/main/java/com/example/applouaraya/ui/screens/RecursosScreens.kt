package com.example.applouaraya.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.model.Sucursal
import com.example.applouaraya.model.SucursalesMock
import com.example.applouaraya.viewmodel.EscanerQRViewModel
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun EscanerQRScreen(vm: EscanerQRViewModel, clienteId: Int?, irDetalle: (Int) -> Unit) {
    val context = LocalContext.current
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val options = remember {
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }
    val scanner = remember(context) { GmsBarcodeScanning.getClient(context, options) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Escáner QR", style = MaterialTheme.typography.headlineLarge)
        Text("Formato de demostración: LOU-PRODUCTO-1")
        Button(onClick = {
            scanner.startScan().addOnSuccessListener { barcode ->
                barcode.rawValue?.let { vm.validar(it, clienteId, irDetalle) }
                    ?: vm.error("QR sin texto")
            }.addOnFailureListener { vm.error("No fue posible abrir el escáner") }
        }) {
            Text("Escanear QR")
        }
        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
fun MapaScreen() {
    val context = LocalContext.current
    var sucursalSeleccionada by remember { mutableStateOf<Sucursal?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Ferias y showrooms (Santiago)",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )

            AndroidView(
                factory = { ctx ->
                    val config = Configuration.getInstance()
                    config.load(ctx, ctx.getSharedPreferences("osmdroid_pref", Context.MODE_PRIVATE))
                    config.userAgentValue = "LouAraya-AppDemo-Estudiante-Chile/2.0 (contacto: admin@louaraya.cl)"

                    MapView(ctx).apply {
                        setTileSource(
                            XYTileSource(
                                "OSM_HOT",
                                0, 19, 256, ".png",
                                arrayOf(
                                    "https://a.tile.openstreetmap.fr/hot/",
                                    "https://b.tile.openstreetmap.fr/hot/"
                                )
                            )
                        )
                        setMultiTouchControls(true)

                        controller.setZoom(13.0)
                        controller.setCenter(GeoPoint(-33.4372, -70.6506))

                        SucursalesMock.lista.forEach { sucursal ->
                            val marker = Marker(this).apply {
                                position = GeoPoint(sucursal.latitud, sucursal.longitud)
                                title = sucursal.nombre
                                snippet = "${sucursal.tipo} - ${sucursal.direccion}, ${sucursal.comuna}"
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                setOnMarkerClickListener { _, _ ->
                                    sucursalSeleccionada = sucursal
                                    true
                                }
                            }
                            overlays.add(marker)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        sucursalSeleccionada?.let { sucursal ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = sucursal.nombre,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = sucursal.tipo,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                    Text(
                        text = "${sucursal.direccion}, ${sucursal.comuna}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Horario: ${sucursal.horario}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    sucursal.fechaEvento?.let { fecha ->
                        Text(
                            text = "Fecha: $fecha",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Button(
                        onClick = { sucursalSeleccionada = null },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cerrar")
                    }
                }
            }
        }
    }
}