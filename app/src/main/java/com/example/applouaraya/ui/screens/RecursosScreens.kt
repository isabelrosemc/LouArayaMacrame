package com.example.applouaraya.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.viewmodel.EscanerQRViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

@Composable
fun EscanerQRScreen(vm: EscanerQRViewModel, clienteId: Int?, irDetalle: (Int) -> Unit) {
    val context = LocalContext.current
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val options = remember { GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE).build() }
    val scanner = remember(context) { GmsBarcodeScanning.getClient(context, options) }
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Escáner QR", style = MaterialTheme.typography.headlineLarge)
        Text("Formato de demostración: LOU-PRODUCTO-1")
        Button(onClick = {
            scanner.startScan().addOnSuccessListener { barcode ->
                barcode.rawValue?.let { vm.validar(it, clienteId, irDetalle) }
                    ?: vm.error("QR sin texto")
            }.addOnFailureListener { vm.error("No fue posible abrir el escáner") }
        }) { Text("Escanear QR") }
        if (mensaje.isNotEmpty()) Text(mensaje, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
fun MapaScreen() {
    val lugar = LatLng(-33.45, -70.66)
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(lugar, 11f)
    }
    Column(Modifier.fillMaxSize()) {
        Text("Ferias y showrooms (demo)", modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium)
        GoogleMap(modifier = Modifier.fillMaxWidth().weight(1f), cameraPositionState = camara) {
            Marker(state = MarkerState(position = lugar),
                title = "Ubicación ficticia", snippet = "No corresponde a una feria real")
        }
    }
}
