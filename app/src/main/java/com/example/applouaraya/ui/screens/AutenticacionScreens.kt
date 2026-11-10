package com.example.applouaraya.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.ui.components.SelectorImagen
import com.example.applouaraya.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(vm: SesionViewModel, irRegistro: () -> Unit) {
    var alias by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = alias,
            onValueChange = { alias = it },
            label = { Text("Alias") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(14.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(10.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { vm.entrar(alias, clave) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = irRegistro) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(vm: SesionViewModel, volver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var alias by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    // Estados de error individuales por campo
    var errorNombre by remember { mutableStateOf<String?>(null) }
    var errorAlias by remember { mutableStateOf<String?>(null) }
    var errorCorreo by remember { mutableStateOf<String?>(null) }
    var errorContrasena by remember { mutableStateOf<String?>(null) }
    var errorConfirmar by remember { mutableStateOf<String?>(null) }

    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Cliente", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        SelectorImagen(
            onImagenSeleccionada = { /* Vista previa en memoria */ }
        )

        // Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                errorNombre = if (it.isBlank()) "El nombre es obligatorio" else null
            },
            label = { Text("Nombre completo *") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            isError = errorNombre != null,
            supportingText = { errorNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                if (errorNombre != null) Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Alias
        OutlinedTextField(
            value = alias,
            onValueChange = {
                alias = it
                errorAlias = if (it.isBlank()) "El alias es obligatorio" else null
            },
            label = { Text("Alias único *") },
            leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
            isError = errorAlias != null,
            supportingText = { errorAlias?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                if (errorAlias != null) Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Correo electrónico con validación estricta (observación del audio)
        OutlinedTextField(
            value = correo,
            onValueChange = {
                correo = it
                errorCorreo = when {
                    it.isBlank() -> "El correo es obligatorio"
                    !vm.esCorreoValido(it) -> "Ingresa un correo válido (ej: usuario@correo.com)"
                    else -> null
                }
            },
            label = { Text("Correo electrónico *") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = errorCorreo != null,
            supportingText = { errorCorreo?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                if (errorCorreo != null) Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Teléfono
        OutlinedTextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Dirección
        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección (opcional)") },
            leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = {
                contrasena = it
                errorContrasena = when {
                    it.isBlank() -> "La contraseña es obligatoria"
                    it.length < 8 -> "Debe tener al menos 8 caracteres"
                    else -> null
                }
                if (confirmarContrasena.isNotBlank() && it != confirmarContrasena) {
                    errorConfirmar = "Las contraseñas no coinciden"
                } else if (confirmarContrasena.isNotBlank()) {
                    errorConfirmar = null
                }
            },
            label = { Text("Contraseña (mínimo 8 caracteres) *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = errorContrasena != null,
            supportingText = { errorContrasena?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                if (errorContrasena != null) Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Confirmar contraseña
        OutlinedTextField(
            value = confirmarContrasena,
            onValueChange = {
                confirmarContrasena = it
                errorConfirmar = when {
                    it.isBlank() -> "Debes repetir la contraseña"
                    it != contrasena -> "Las contraseñas no coinciden"
                    else -> null
                }
            },
            label = { Text("Repetir contraseña *") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = errorConfirmar != null,
            supportingText = { errorConfirmar?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
            trailingIcon = {
                if (errorConfirmar != null) Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (mensaje.isNotEmpty()) {
            Text(mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val hayErrorNombre = nombre.isBlank()
                val hayErrorAlias = alias.isBlank()
                val hayErrorCorreo = !vm.esCorreoValido(correo)
                val hayErrorClave = contrasena.length < 8
                val hayErrorConfirmar = contrasena != confirmarContrasena

                errorNombre = if (hayErrorNombre) "El nombre es obligatorio" else null
                errorAlias = if (hayErrorAlias) "El alias es obligatorio" else null
                errorCorreo = if (hayErrorCorreo) "Ingresa un correo válido" else null
                errorContrasena = if (hayErrorClave) "Mínimo 8 caracteres" else null
                errorConfirmar = if (hayErrorConfirmar) "Las contraseñas no coinciden" else null

                if (!hayErrorNombre && !hayErrorAlias && !hayErrorCorreo && !hayErrorClave && !hayErrorConfirmar) {
                    vm.registrar(
                        nombre = nombre,
                        alias = alias,
                        correo = correo,
                        telefono = telefono,
                        direccion = direccion,
                        clave = contrasena,
                        confirmarClave = confirmarContrasena,
                        onExito = volver
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }

        TextButton(onClick = volver) {
            Text("Ya tengo cuenta. Volver al Login")
        }
    }
}