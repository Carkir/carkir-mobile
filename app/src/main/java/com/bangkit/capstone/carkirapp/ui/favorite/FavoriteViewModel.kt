package com.bangkit.capstone.carkirapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repo: AppRepository) : ViewModel() {

    fun loadAllFavoritePlaces() = repo.getAllFavoritePlaces().asLiveData()

    fun deleteFavoritePlace(place: FavoriteEntity) {
        viewModelScope.launch {
            repo.deleteFavoritePlace(place)
        }
    }
}