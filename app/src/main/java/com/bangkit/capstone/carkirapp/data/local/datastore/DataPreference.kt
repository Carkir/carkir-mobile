package com.bangkit.capstone.carkirapp.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPreference private constructor(private val dataStore: DataStore<Preferences>) {

    /**
     * Get token from data store
     * */
    fun loadToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[tokenKey] ?: ""
        }
    }

    /**
     * Get user state is first time using this apps
     * This state determine is user have to skip or stay in on boarding activity
     * */
    fun loadStateFirstTime(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[isFirsTime] ?: true
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
     * Update user state is first time
     * This state determine is user have to skip or stay in on boarding activity
     * */
    suspend fun updateStateFirstTime(newState: Boolean) {
        dataStore.edit { preferences ->
            preferences[isFirsTime] = newState
        }
    }

    /**
     * Singleton pattern, only create once
     * */
    companion object {
        @Volatile
        private var instance: DataPreference? = null
        private val tokenKey = stringPreferencesKey("token")
        private val isFirsTime = booleanPreferencesKey("isFirstTime")

        fun getInstance(dataStore: DataStore<Preferences>): DataPreference {
            return instance ?: synchronized(this) {
                val dataPreference = DataPreference(dataStore)
                instance = dataPreference
                dataPreference
            }
        }
    }
}