// File: app/src/main/java/com/rahul/auric/fintrack/auricfin/ui/theme/Theme.kt
package com.rahul.auric.fintrack.auricfin.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- THIS IS THE CORRECTED CODE FOR CUSTOM COLORS ---

// 1. Define a data class to hold our custom color.
data class AppColorScheme(
    val income: Color
)

// 2. Define our custom light and dark color schemes.
private val lightAppColorScheme = AppColorScheme(
    income = IncomeColorLight
)

private val darkAppColorScheme = AppColorScheme(
    income = IncomeColorDark
)

// 3. Create a CompositionLocal to provide our custom scheme.
private val LocalAppColorScheme = staticCompositionLocalOf { lightAppColorScheme }

// 4. Create an accessor object to easily get our custom colors.
object AppTheme {
    val extendedColors: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColorScheme.current
}
// --- END OF CORRECTED CODE ---


// These are the standard Material color schemes
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    error = ErrorDark,
    outline = OutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    secondary = SecondaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorLight,
    outline = OutlineLight
)


@Composable
fun AuricFinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Determine which custom color scheme to use
    val appColorScheme = if (darkTheme) darkAppColorScheme else lightAppColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb() // Make status bar transparent
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    // Provide both the standard and our custom color schemes to the app
    CompositionLocalProvider(LocalAppColorScheme provides appColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}