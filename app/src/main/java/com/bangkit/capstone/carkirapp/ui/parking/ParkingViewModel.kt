package com.bangkit.capstone.carkirapp.ui.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ParkingViewModel(private val repo: AppRepository) : ViewModel() {
    fun loadOccupancyFloor(token: String, name: String, floor: Int) = repo.getOccupancyParkingPlace(token, name, floor).asLiveData()
    fun loadTokenFromDataStore() = repo.getTokenFromDataStore().asLiveData()

    fun requestToken() {
        viewModelScope.launch {
            repo.tokenKey()
        }
    }
}