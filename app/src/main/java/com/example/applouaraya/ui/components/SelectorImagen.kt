package com.example.applouaraya.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SelectorImagen(onImagenSeleccionada: (Bitmap) -> Unit) {
    val context = LocalContext.current
    var imagen by remember { mutableStateOf<Bitmap?>(null) }
    var mensaje by remember { mutableStateOf("") }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) { imagen = bitmap; onImagenSeleccionada(bitmap) }
        else mensaje = "No se capturó fotografía"
    }
    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                val bmp = BitmapFactory.decodeStream(stream)
                if (bmp != null) { imagen = bmp; onImagenSeleccionada(bmp) }
                else mensaje = "No se pudo leer la imagen"
            }
        } catch (_: Exception) { mensaje = "Error al abrir imagen" }
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        imagen?.let {
            Image(it.asImageBitmap(), contentDescription = "Vista previa de imagen",
                modifier = Modifier.fillMaxWidth().height(150.dp), contentScale = ContentScale.Crop)
        }
        OutlinedButton(onClick = { camara.launch(null) }) { Text("Tomar foto (vista previa)") }
        OutlinedButton(onClick = { galeria.launch("image/*") }) { Text("Seleccionar imagen") }
        if (mensaje.isNotEmpty()) Text(mensaje)
    }
}
