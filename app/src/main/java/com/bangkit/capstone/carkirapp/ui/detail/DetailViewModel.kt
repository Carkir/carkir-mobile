package com.bangkit.capstone.carkirapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class DetailViewModel(private val repo: AppRepository) : ViewModel() {

    fun loadDetailParkingPlace(name: String) = repo.getParkingPlace(name).asLiveData()

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