package com.example.applouaraya.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores del Mockup Lou Araya Macramé
val Terracota = Color(0xFFB85D38)
val TerracotaOscuro = Color(0xFF9E4B28)
val TerracotaClaro = Color(0xFFFBECE5)
val FondoLino = Color(0xFFFBF7F2)
val ContenedorBeige = Color(0xFFF3ECE2)
val BordeSuave = Color(0xFFE2D6C8)
val TextoTierra = Color(0xFF2D231E)
val TextoSecundario = Color(0xFF7A6E65)

private val LouArayaColorScheme = lightColorScheme(
    primary = Terracota,
    onPrimary = Color.White,
    primaryContainer = TerracotaClaro,
    onPrimaryContainer = TerracotaOscuro,
    surface = FondoLino,
    onSurface = TextoTierra,
    surfaceVariant = ContenedorBeige,
    onSurfaceVariant = TextoSecundario,
    outline = BordeSuave,
    background = FondoLino,
    onBackground = TextoTierra
)

@Composable
fun LouArayaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LouArayaColorScheme,
        content = content
    )
}