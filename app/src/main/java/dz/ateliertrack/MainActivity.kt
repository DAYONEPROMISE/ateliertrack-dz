package dz.ateliertrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import dz.ateliertrack.ui.AtelierNav
import dz.ateliertrack.ui.TicketViewModel
import dz.ateliertrack.ui.theme.AtelierTrackTheme
import dz.ateliertrack.util.LocaleHelper
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as AtelierTrackApp
        setContent {
            val lang by LocaleHelper.observeLang(this).collectAsState(initial = LocaleHelper.FRENCH)
            val effective = lang.ifBlank { LocaleHelper.FRENCH }
            applyAppLocale(effective)
            val dark = isSystemInDarkTheme()
            val vm: TicketViewModel = viewModel(factory = TicketViewModel.factory(app.repository))
            AtelierTrackTheme(darkTheme = dark) {
                AtelierNav(viewModel = vm, lang = effective)
            }
        }
    }

    private fun applyAppLocale(lang: String) {
        Locale.setDefault(Locale(lang))
        resources.configuration.setLocale(Locale(lang))
        @Suppress("DEPRECATION")
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)
    }
}
