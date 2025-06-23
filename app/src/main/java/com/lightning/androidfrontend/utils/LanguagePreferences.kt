package com.lightning.androidfrontend.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Locale

object LanguagePreferences {
    fun Context.updateLocale(locale: Locale): Context {
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return createConfigurationContext(config)
    }

    private val LANGUAGE_KEY = stringPreferencesKey("app_language")

    val Context.dataStore by preferencesDataStore("settings")

    fun save(context: Context, languageCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[LANGUAGE_KEY] = languageCode
            }
        }
    }

    fun get(context: Context): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[LANGUAGE_KEY]
        }

    fun clear(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit {
                it.remove(LANGUAGE_KEY)
            }
        }
    }
}