package com.example.applouaraya.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.applouaraya.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    vm: SesionViewModel,
    clienteId: Int? = null,
    irSalir: () -> Unit = {},
    irNotificaciones: () -> Unit = {}
) {
    val cliente by vm.clienteAutenticado.collectAsStateWithLifecycle()
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()

    var modoEdicion by remember { mutableStateOf(false) }
    var mostrarDialogoPassword by remember { mutableStateOf(false) }

    // Estados de edición de datos personales
    var nombre by remember(cliente) { mutableStateOf(cliente?.nombre ?: "") }
    var correo by remember(cliente) { mutableStateOf(cliente?.correo ?: "") }
    var telefono by remember(cliente) { mutableStateOf(cliente?.telefono ?: "") }
    var direccion by remember(cliente) { mutableStateOf(cliente?.direccion ?: "") }
    var errorLocal by remember { mutableStateOf<String?>(null) }

    // Estados para el cambio de contraseña
    var passActual by remember { mutableStateOf("") }
    var passNueva by remember { mutableStateOf("") }
    var passConfirmar by remember { mutableStateOf("") }
    var errorDialogo by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = {
                        if (modoEdicion) {
                            nombre = cliente?.nombre ?: ""
                            correo = cliente?.correo ?: ""
                            telefono = cliente?.telefono ?: ""
                            direccion = cliente?.direccion ?: ""
                            errorLocal = null
                        }
                        modoEdicion = !modoEdicion
                    }) {
                        Icon(
                            imageVector = if (modoEdicion) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (modoEdicion) "Cancelar" else "Editar Perfil"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(54.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Text(
                text = "@${cliente?.alias ?: "usuario"}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.outline
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Datos Personales
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it; errorLocal = null },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                enabled = modoEdicion,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it; errorLocal = null },
                label = { Text("Correo electrónico") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                enabled = modoEdicion,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Teléfono de contacto") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                enabled = modoEdicion,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección registrada") },
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) },
                enabled = modoEdicion,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorLocal != null) {
                Text(errorLocal!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            if (mensaje.isNotEmpty()) {
                Text(mensaje, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (modoEdicion) {
                Button(
                    onClick = {
                        if (nombre.isBlank() || correo.isBlank()) {
                            errorLocal = "Nombre y correo son obligatorios"
                        } else if (!vm.esCorreoValido(correo)) {
                            errorLocal = "Formato de correo inválido"
                        } else {
                            vm.actualizarPerfil(nombre, correo, telefono, direccion) { exitoso ->
                                if (exitoso) modoEdicion = false
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Guardar Cambios")
                }
            } else {
                // Botón para desplegar cambio de contraseña (observación del audio)
                OutlinedButton(
                    onClick = {
                        passActual = ""
                        passNueva = ""
                        passConfirmar = ""
                        errorDialogo = null
                        mostrarDialogoPassword = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cambiar Contraseña")
                }

                OutlinedButton(
                    onClick = irSalir,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Diálogo modal para cambio de contraseña
        if (mostrarDialogoPassword) {
            AlertDialog(
                onDismissRequest = { mostrarDialogoPassword = false },
                title = { Text("Cambiar Contraseña") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedTextField(
                            value = passActual,
                            onValueChange = { passActual = it },
                            label = { Text("Contraseña actual") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = passNueva,
                            onValueChange = { passNueva = it },
                            label = { Text("Nueva contraseña (mín 8 car.)") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = passConfirmar,
                            onValueChange = { passConfirmar = it },
                            label = { Text("Confirmar nueva contraseña") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorDialogo != null) {
                            Text(
                                text = errorDialogo!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            vm.cambiarContrasena(passActual, passNueva, passConfirmar) { exito, msg ->
                                if (exito) {
                                    mostrarDialogoPassword = false
                                } else {
                                    errorDialogo = msg
                                }
                            }
                        }
                    ) {
                        Text("Actualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogoPassword = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}