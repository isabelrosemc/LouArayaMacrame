package com.example.applouaraya.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Crema = Color(0xFFFFF9F3)
private val Terracota = Color(0xFFA94F35)
private val Cafe = Color(0xFF4B3029)
private val colores = lightColorScheme(
    primary = Terracota,
    onPrimary = Color.White,
    background = Crema,
    onBackground = Cafe,
    surface = Crema,
    onSurface = Cafe
)
@Composable
fun LouArayaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = colores, content = content)
}
