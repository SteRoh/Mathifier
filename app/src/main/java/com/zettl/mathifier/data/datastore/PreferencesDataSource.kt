package com.zettl.mathifier.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mathifier_preferences")

private object Keys {
    val currentProfileId = longPreferencesKey("current_profile_id")
}

class PreferencesDataSource(private val context: Context) {

    val currentProfileId: Flow<Long?> = context.dataStore.data.map { prefs ->
        val value = prefs[Keys.currentProfileId]
        if (value != null && value > 0L) value else null
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
}
