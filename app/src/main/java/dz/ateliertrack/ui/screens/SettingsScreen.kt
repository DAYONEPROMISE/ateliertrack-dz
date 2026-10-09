package dz.ateliertrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dz.ateliertrack.util.LocaleHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(lang: String, onBack: () -> Unit) {
    val fr = lang != "ar"
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (fr) "Parametres" else "الإعدادات") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, null) } }
            )
        }
    ) { pad ->
        Column(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(if (fr) "Langue / اللغة" else "اللغة / Langue")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = fr,
                    onClick = { scope.launch { LocaleHelper.setLang(context, LocaleHelper.FRENCH) } },
                    label = { Text("Francais") }
                )
                FilterChip(
                    selected = !fr,
                    onClick = { scope.launch { LocaleHelper.setLang(context, LocaleHelper.ARABIC) } },
                    label = { Text("العربية (RTL)") }
                )
            }
            Text(if (fr) "100% hors-ligne. Aucun compte. Donnees sur l'appareil uniquement." else "يعمل دون إنترنت. بدون حساب. البيانات على الجهاز فقط.")
        }
    }
}
