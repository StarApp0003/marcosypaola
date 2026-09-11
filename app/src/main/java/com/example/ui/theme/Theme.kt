package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = SoftRose,
    onPrimary = WineDark,
    primaryContainer = DeepBurgundy,
    onPrimaryContainer = BlushPink,
    secondary = ChampagneGold,
    onSecondary = WineDark,
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = LightGold,
    tertiary = LightGold,
    background = RomanticDarkBackground,
    onBackground = TextLight,
    surface = RomanticDarkSurface,
    onSurface = TextLight,
    surfaceVariant = RomanticDarkCard,
    onSurfaceVariant = TextLightMuted
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RoseRed,
    onPrimary = Color.White,
    primaryContainer = RoseMist,
    onPrimaryContainer = WineDark,
    secondary = DeepBurgundy,
    onSecondary = Color.White,
    secondaryContainer = BlushPink,
    onSecondaryContainer = DeepBurgundy,
    tertiary = ChampagneGold,
    background = WarmIvory,
    onBackground = TextDark,
    surface = WarmCard,
    onSurface = TextDark,
    surfaceVariant = BlushPink,
    onSurfaceVariant = TextMuted
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

