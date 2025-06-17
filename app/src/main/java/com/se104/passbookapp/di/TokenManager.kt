package com.se104.passbookapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {


    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    suspend fun saveToken(accessToken: String, refreshToken: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[ACCESS_TOKEN] = accessToken
                preferences[REFRESH_TOKEN] = refreshToken
            }
        }
    }

    suspend fun deleteToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences.remove(ACCESS_TOKEN)
                preferences.remove(REFRESH_TOKEN)
            }
        }
    }

    fun getAccessToken(): Flow<String?> {
        return dataStore.data
            .flowOn(Dispatchers.IO)
            .map { preferences ->
                preferences[ACCESS_TOKEN]
            }
    }

    fun getRefreshToken(): Flow<String?> {
        return dataStore.data
            .flowOn(Dispatchers.IO)
            .map { preferences ->
                preferences[REFRESH_TOKEN]
            }
    }

}