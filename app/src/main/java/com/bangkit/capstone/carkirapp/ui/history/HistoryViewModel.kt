package com.bangkit.capstone.carkirapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repo: AppRepository) : ViewModel() {
    fun loadAllHistory() = repo.getHistoriesPlace().asLiveData()

    fun removeHistoryPlace(place: PlacesEntity, newState: Boolean) {
        viewModelScope.launch {
            place.isAlreadySee = newState
            repo.updateHistory(place)
        }
    }

    fun removeHistories() {
        viewModelScope.launch {
            repo.deleteHistories()
        }
    }
}