package dz.ateliertrack.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * AtelierTrack "Atelier" identity — warm graphite canvas with signal amber
 * accents. Tokens from Shared/OpenPencil/ateliertrack-dz/design-v2.html.
 */
private val AtelierLight = lightColorScheme(
    primary = Color(0xFF37474F),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCE4E8),
    onPrimaryContainer = Color(0xFF121C21),
    secondary = Color(0xFF5A6B72),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDDE6EA),
    onSecondaryContainer = Color(0xFF172227),
    tertiary = Color(0xFFB36B0E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE3C2),
    onTertiaryContainer = Color(0xFF2E1D00),
    error = Color(0xFFB3261E),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
    background = Color(0xFFF6F4F1),
    onBackground = Color(0xFF1B1C1E),
    surface = Color(0xFFF6F4F1),
    onSurface = Color(0xFF1B1C1E),
    surfaceVariant = Color(0xFFE7E3DD),
    onSurfaceVariant = Color(0xFF48464A),
    outline = Color(0xFF79767C),
    outlineVariant = Color(0xFFC9C5CC)
)

private val AtelierDark = darkColorScheme(
    primary = Color(0xFFB0BEC5),
    onPrimary = Color(0xFF1E2C33),
    primaryContainer = Color(0xFF2E3F47),
    onPrimaryContainer = Color(0xFFDCE4E8),
    secondary = Color(0xFFB6C6CD),
    onSecondary = Color(0xFF21343B),
    secondaryContainer = Color(0xFF384A52),
    onSecondaryContainer = Color(0xFFDDE6EA),
    tertiary = Color(0xFFF2BE72),
    onTertiary = Color(0xFF432C00),
    tertiaryContainer = Color(0xFF6B4300),
    onTertiaryContainer = Color(0xFFFFE3C2),
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
    background = Color(0xFF14171A),
    onBackground = Color(0xFFE3E1E5),
    surface = Color(0xFF14171A),
    onSurface = Color(0xFFE3E1E5),
    surfaceVariant = Color(0xFF1C2024),
    onSurfaceVariant = Color(0xFFC7C5CA),
    outline = Color(0xFF918F95),
    outlineVariant = Color(0xFF48464A)
)

@Composable
fun AtelierTrackTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) AtelierDark else AtelierLight,
        typography = MaterialTheme.typography,
        content = content
    )
}

/**
 * Brand app bar. Surface + status-bar padding keeps it stable across
 * Material3 versions. Pass [navigateUp] to show a back arrow.
 */
@Composable
fun AtelierHeader(
    title: String,
    modifier: Modifier = Modifier,
    navigateUp: (() -> Unit)? = null,
    backLabel: String = "",
    trailing: (@Composable () -> Unit)? = null
) {
    Surface(color = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, modifier = modifier) {
        Row(
            Modifier.fillMaxWidth().statusBarsPadding().height(64.dp).padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (navigateUp != null) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = backLabel
                    )
                }
            }
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f).padding(start = if (navigateUp != null) 4.dp else 8.dp)
            )
            trailing?.invoke()
        }
    }
}

/** Elevated card on the warm canvas. */
@Composable
fun AtelierCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp)) { content() }
    }
}

/** Empty state with a tinted amber badge; the glyph is decorative. */
@Composable
fun AtelierEmptyState(icon: ImageVector, title: String, hint: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(50),
            modifier = Modifier.size(96.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = hint,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
