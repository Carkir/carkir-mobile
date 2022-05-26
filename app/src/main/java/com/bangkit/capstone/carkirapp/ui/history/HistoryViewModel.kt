package com.bangkit.capstone.carkirapp.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(private val repo: AppRepository) : ViewModel() {

    fun loadAllHistory() = repo.getAllHistoryPlaces().asLiveData()

    fun deleteHistoryPlace(place: HistoryEntity) {
        viewModelScope.launch {
            repo.deleteHistoryPlace(place)
        }
    }

    fun deleteAllHistory() {
        viewModelScope.launch {
            repo.deleteAllHistory()
        }
    }

}