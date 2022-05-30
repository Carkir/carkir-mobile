package com.bangkit.capstone.carkirapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: AppRepository) : ViewModel() {

    fun loadDetailParkingPlace(token: String, name: String) = repo.getParkingPlace(token, name).asLiveData()
    fun loadTokenFromDataStore() = repo.getTokenFromDataStore().asLiveData()

    fun requestToken() {
        viewModelScope.launch {
            repo.tokenKey()
        }
    }

    // Operation to add parking place to history local database
    fun addPlaceToHistory(place: HistoryEntity) {
        viewModelScope.launch {
            repo.addPlaceToHistory(place)
        }
    }

    // Operation to add parking place to local database
    fun addPlaceToFavorite(place: FavoriteEntity) {
        viewModelScope.launch {
            repo.insertFavoritePlace(place)
        }
    }

    // Operation to remove parking place from local database
    fun removePlaceFromFavorite(place: FavoriteEntity) {
        viewModelScope.launch {
            repo.deleteFavoritePlace(place)
        }
    }
}