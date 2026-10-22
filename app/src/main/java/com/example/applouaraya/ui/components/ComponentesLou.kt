package com.example.applouaraya.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BotonLou(texto: String, onClick: () -> Unit, habilitado: Boolean = true) {
    Button(onClick = onClick, enabled = habilitado, modifier = Modifier.fillMaxWidth()) {
        Text(texto)
    }
}
@Composable
fun TarjetaLou(titulo: String, descripcion: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleLarge)
            Text(descripcion)
        }
    }
}
