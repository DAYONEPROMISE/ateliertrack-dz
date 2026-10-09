package dz.ateliertrack.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF006A60),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF9CF2E5),
    secondary = Color(0xFF4A635E),
    tertiary = Color(0xFF456179),
    error = Color(0xFFBA1A1A),
    background = Color(0xFFFAFDFC),
    surface = Color(0xFFFAFDFC)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF80D5C8),
    onPrimary = Color(0xFF003731),
    primaryContainer = Color(0xFF005049),
    secondary = Color(0xFFB1CCC6),
    tertiary = Color(0xFFAECBDE),
    error = Color(0xFFFFB4AB),
    background = Color(0xFF191C1C),
    surface = Color(0xFF191C1C)
)

@Composable
fun AtelierTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography(),
        content = content
    )
}
