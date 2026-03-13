package com.zettl.mathifier.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mathifier_preferences")

private object Keys {
    val currentProfileId = longPreferencesKey("current_profile_id")
    val language = stringPreferencesKey("language")
}

const val LANGUAGE_SYSTEM = "system"
const val LANGUAGE_DE = "de"
const val LANGUAGE_EN = "en"

class PreferencesDataSource(private val context: Context) {

    val currentProfileId: Flow<Long?> = context.dataStore.data.map { prefs ->
        val value = prefs[Keys.currentProfileId]
        if (value != null && value > 0L) value else null
    }

    val language: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.language] ?: LANGUAGE_SYSTEM
    }

    suspend fun setCurrentProfileId(profileId: Long?) {
        context.dataStore.edit { prefs ->
            if (profileId != null) {
                prefs[Keys.currentProfileId] = profileId
            } else {
                prefs.remove(Keys.currentProfileId)
            }
        }
    }

    fun getLanguageBlocking(): String = runBlocking {
        context.dataStore.data.first()[Keys.language] ?: LANGUAGE_SYSTEM
    }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.language] = language
        }
    }
}
