package com.bangkit.capstone.carkirapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenPreference private constructor(private val dataStore: DataStore<Preferences>) {

    /**
     * Get token from data store
     * */
    fun loadToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    /**
     * Update token from server
     * */
    suspend fun updateToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    /**
     * Singleton pattern, only create once
     * */
    companion object {
        @Volatile
        private var instance: TokenPreference? = null
        private val tokenKey = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreference {
            return instance ?: synchronized(this) {
                val tokenPreference = TokenPreference(dataStore)
                instance = tokenPreference
                tokenPreference
            }
        }
    }
}