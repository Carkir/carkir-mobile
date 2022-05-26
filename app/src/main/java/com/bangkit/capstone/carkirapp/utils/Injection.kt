package com.bangkit.capstone.carkirapp.utils

import android.content.Context
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.room.LocationDatabase
import com.bangkit.capstone.carkirapp.data.remote.network.ApiConfig

/**
 * Using Injection for provide the repository on ViewModelFactory
 * */
object Injection {
    fun provideRepository(context: Context): AppRepository {
        val apiService = ApiConfig.provideApiService()
        val database = LocationDatabase.getInstance(context)
        val dao = database.locationDao()
        return AppRepository.getInstance(apiService, dao)
    }
}