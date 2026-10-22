package com.example.applouaraya.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(clienteId: Int, irSalir: () -> Unit, irNotificaciones: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Mi perfil", style = MaterialTheme.typography.headlineLarge)
        Text("Cliente local #$clienteId")
        Text("Edición de datos y avatar permanente: pendiente")
        OutlinedButton(onClick = irNotificaciones) { Text("Notificaciones") }
        Button(onClick = irSalir) { Text("Cerrar sesión") }
    }
}
