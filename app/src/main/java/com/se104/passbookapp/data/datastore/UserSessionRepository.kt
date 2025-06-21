package com.se104.passbookapp.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    object UserPreferencesKeys {

        val PERMISSIONS = stringPreferencesKey("permissions")
        val USER_ID = longPreferencesKey("user_id")
    }

    suspend fun saveUserSession(userId: Long, permissions: List<String>) {
        Log.d("userSession", permissions.toString())
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = userId
            prefs[UserPreferencesKeys.PERMISSIONS] = permissions.joinToString(",")
        }

    }

    suspend fun getUserId(): Long? {
        val prefs = dataStore.data.first()
        return prefs[UserPreferencesKeys.USER_ID]
    }



    val permissionsFlow: Flow<List<String>> = dataStore.data.map { prefs ->
        val permissionsString = prefs[UserPreferencesKeys.PERMISSIONS]
        permissionsString?.split(",") ?: emptyList()
    }





    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(UserPreferencesKeys.USER_ID)
            prefs.remove(UserPreferencesKeys.PERMISSIONS)

        }
    }
}