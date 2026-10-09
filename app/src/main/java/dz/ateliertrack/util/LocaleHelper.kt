package dz.ateliertrack.util

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

/** App language preference: "fr" (default) or "ar" (RTL). Persisted with DataStore, offline. */
object LocaleHelper {
    const val FRENCH = "fr"
    const val ARABIC = "ar"
    private val LANG_KEY = stringPreferencesKey("lang")

    fun observeLang(context: Context): Flow<String> =
        context.dataStore.data.map { it[LANG_KEY] ?: FRENCH }

    suspend fun setLang(context: Context, lang: String) {
        context.dataStore.edit { it[LANG_KEY] = if (lang == ARABIC) ARABIC else FRENCH }
    }

    fun isRtl(lang: String): Boolean = lang == ARABIC
}
