package com.bangkit.capstone.carkirapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.bangkit.capstone.carkirapp.data.AppRepository

class HomeViewModel(private val repo: AppRepository) : ViewModel() {
    fun getAllParkingPlaces() = repo.getAllParkingPlace().asLiveData()
    fun getRecentParkingPlaces() = repo.getRecentParkingPlace().asLiveData()
}