package com.example.applouaraya.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.viewmodel.SesionViewModel
import com.example.applouaraya.ui.components.SelectorImagen

@Composable
fun RegistroScreen(vm: SesionViewModel, volver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
        @Composable fun Campo(valor: String, cambio: (String) -> Unit, etiqueta: String) {
            OutlinedTextField(value = valor, onValueChange = cambio, label = { Text(etiqueta) },
                modifier = Modifier.fillMaxWidth())
        }
        Campo(nombre, { nombre = it }, "Nombre")
        Campo(alias, { alias = it }, "Alias")
        Campo(correo, { correo = it }, "Correo")
        Campo(telefono, { telefono = it }, "Teléfono (opcional)")
        Campo(direccion, { direccion = it }, "Dirección ficticia (opcional)")
        OutlinedTextField(value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña (mínimo 8)") }, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())
        Text("Fotografía de perfil: vista previa, sin guardar en Room")
        SelectorImagen(onImagenSeleccionada = {})
        Button(onClick = { vm.registrar(nombre, alias, correo, telefono, direccion, clave, volver) },
            modifier = Modifier.fillMaxWidth()) { Text("Registrar") }
        if (mensaje.isNotEmpty()) Text(mensaje)
        TextButton(onClick = volver) { Text("Volver") }
    }
}

@Composable
fun LoginScreen(vm: SesionViewModel, irRegistro: () -> Unit) {
    var alias by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center,
        ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = alias, onValueChange = { alias = it },
            label = { Text("Alias") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth())
        Button(onClick = { vm.entrar(alias, clave) }, modifier = Modifier.fillMaxWidth()) { Text("Ingresar") }
        TextButton(onClick = irRegistro) { Text("Crear cuenta") }
        if (mensaje.isNotEmpty()) Text(mensaje, color = MaterialTheme.colorScheme.error)
    }
}
