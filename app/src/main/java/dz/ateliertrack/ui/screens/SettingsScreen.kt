package dz.ateliertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dz.ateliertrack.R
import dz.ateliertrack.ui.theme.AtelierCard
import dz.ateliertrack.ui.theme.AtelierHeader
import dz.ateliertrack.util.LocaleHelper
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(lang: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val ar = lang == LocaleHelper.ARABIC

    AtelierHeader(
        title = stringResource(R.string.settings_title),
        navigateUp = onBack,
        backLabel = stringResource(R.string.action_back)
    )
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AtelierCard {
            Text(
                stringResource(R.string.settings_language),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                FilterChip(
                    selected = !ar,
                    onClick = { scope.launch { LocaleHelper.setLang(context, LocaleHelper.FRENCH) } },
                    label = { Text(stringResource(R.string.settings_lang_fr), style = MaterialTheme.typography.labelLarge) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                FilterChip(
                    selected = ar,
                    onClick = { scope.launch { LocaleHelper.setLang(context, LocaleHelper.ARABIC) } },
                    label = { Text(stringResource(R.string.settings_lang_ar), style = MaterialTheme.typography.labelLarge) },
                    shape = RoundedCornerShape(50),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
        AtelierCard {
            Text(
                stringResource(R.string.settings_about),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
