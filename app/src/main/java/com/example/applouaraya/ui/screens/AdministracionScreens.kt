package com.example.applouaraya.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GestionProductosScreen() {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var dimensiones by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var disponible by remember { mutableStateOf(true) }
    var mensaje by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Gestión de productos (formulario demo)", style = MaterialTheme.typography.headlineMedium)
        @Composable fun Campo(valor: String, cambio: (String) -> Unit, etiqueta: String) {
            OutlinedTextField(valor, cambio, label = { Text(etiqueta) }, modifier = Modifier.fillMaxWidth())
        }
        Campo(nombre, { nombre = it }, "Nombre")
        Campo(descripcion, { descripcion = it }, "Descripción")
        Campo(categoria, { categoria = it }, "Categoría")
        Campo(material, { material = it }, "Material")
        Campo(dimensiones, { dimensiones = it }, "Dimensiones")
        Campo(precio, { precio = it }, "Precio CLP")
        Row { Text("Disponible"); Switch(disponible, { disponible = it }) }
        Button(onClick = {
            mensaje = if (nombre.isBlank() || categoria.isBlank() ||
                (precio.toIntOrNull() ?: 0) <= 0) "Corrige nombre, categoría y precio"
            else "Formulario válido. Publicación pendiente de backend."
        }) { Text("Validar formulario") }
        Text(mensaje)
    }
}
@Composable
fun GestionCampanasScreen() {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Colecciones y campañas", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(nombre, { nombre = it }, label = { Text("Campaña") })
        OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
        Button(onClick = {}, enabled = false) { Text("Publicar (requiere backend)") }
    }
}
