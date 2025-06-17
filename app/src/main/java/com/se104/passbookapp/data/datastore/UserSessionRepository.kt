package com.se104.passbookapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.se104.passbookapp.utils.Role
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserSessionRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    object UserPreferencesKeys {
        val ROLE = stringPreferencesKey("user_role")
        val USER_ID = longPreferencesKey("user_id")
    }
    suspend fun saveRole(role: Role) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.ROLE] = role.name
        }
    }

    suspend fun getRole(): Role? {
        val prefs = dataStore.data.first()
        val roleName = prefs[UserPreferencesKeys.ROLE] ?: return null
        return Role.entries.find { it.name == roleName }
    }
    suspend fun saveUserId(userId: Long) {
        dataStore.edit { prefs ->
            prefs[UserPreferencesKeys.USER_ID] = userId
        }
    }
    suspend fun getUserId(): Long? {
        val prefs = dataStore.data.first()
        return prefs[UserPreferencesKeys.USER_ID]
    }



    suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.remove(UserPreferencesKeys.ROLE)
            prefs.remove(UserPreferencesKeys.USER_ID)
        }
    }
}