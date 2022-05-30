package com.bangkit.capstone.carkirapp.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.ui.detail.DetailViewModel
import com.bangkit.capstone.carkirapp.ui.favorite.FavoriteViewModel
import com.bangkit.capstone.carkirapp.ui.history.HistoryViewModel
import com.bangkit.capstone.carkirapp.ui.home.HomeViewModel
import com.bangkit.capstone.carkirapp.ui.parking.ParkingViewModel
import com.bangkit.capstone.carkirapp.utils.Injection

class ViewModelFactory private constructor(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ParkingViewModel::class.java) -> {
                ParkingViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context, dataStore: DataStore<Preferences>): ViewModelFactory {
            return instance ?: synchronized(this) {
                val viewModelFactory = ViewModelFactory(Injection.provideRepository(context, dataStore))
                instance = viewModelFactory
                viewModelFactory
            }
        }
    }
}