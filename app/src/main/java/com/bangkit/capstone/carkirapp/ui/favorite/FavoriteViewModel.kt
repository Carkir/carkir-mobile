package com.bangkit.capstone.carkirapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: AppRepository) : ViewModel() {
    fun loadAllFavoritePlaces() = repo.getFavoritesPlace().asLiveData()

    fun removeFavoritePlaces(place: PlacesEntity, newState: Boolean) {
        viewModelScope.launch {
            place.isFavorite = newState
            repo.updateFavorite(place)
        }
    }
}