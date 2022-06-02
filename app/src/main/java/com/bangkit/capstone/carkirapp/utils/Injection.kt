package com.bangkit.capstone.carkirapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.datastore.DataPreference
import com.bangkit.capstone.carkirapp.data.local.room.ParkingDatabase
import com.bangkit.capstone.carkirapp.data.remote.network.ApiConfig

/**
 * Using Injection for provide the repository on ViewModelFactory
 * */
object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): AppRepository {
        val apiService = ApiConfig.provideApiService()
        val preference = DataPreference.getInstance(dataStore)
        val database = ParkingDatabase.getInstance(context)
        val dao = database.placeDao()
        return AppRepository.getInstance(apiService, dao, preference)
    }
}