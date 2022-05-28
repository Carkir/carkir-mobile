package com.bangkit.capstone.carkirapp.ui.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.capstone.carkirapp.data.AppRepository
import kotlinx.coroutines.flow.map

class ParkingViewModel(private val repo: AppRepository) : ViewModel() {
    fun loadOccupancyFloor(name: String, floor: Int) = repo.getOccupancyParkingPlace(name, floor).asLiveData()
}