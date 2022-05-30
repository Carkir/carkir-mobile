package com.bangkit.capstone.carkirapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.carkirapp.data.AppRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: AppRepository) : ViewModel() {
    fun getAllParkingPlaces(token: String) = repo.getAllParkingPlace(token).asLiveData()
    fun getRecentParkingPlaces() = repo.getRecentParkingPlace().asLiveData()
    fun loadTokenFromDataStore() = repo.getTokenFromDataStore().asLiveData()

    fun requestToken() {
        viewModelScope.launch {
            repo.tokenKey()
        }
    }
}