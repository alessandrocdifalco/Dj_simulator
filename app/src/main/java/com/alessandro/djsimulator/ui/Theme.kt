package com.alessandro.djsimulator.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Neon = Color(0xFFFFB000)
val Ink = Color(0xFF09090C)
val Panel = Color(0xFF18181F)

@Composable fun DjTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = darkColorScheme(primary = Neon, background = Ink, surface = Panel, onPrimary = Ink), content = content)
}
