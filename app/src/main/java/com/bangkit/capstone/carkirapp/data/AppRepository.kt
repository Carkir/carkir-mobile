package com.bangkit.capstone.carkirapp.data

import android.util.Log
import com.bangkit.capstone.carkirapp.data.local.datastore.DataPreference
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import com.bangkit.capstone.carkirapp.data.local.room.PlaceDao
import com.bangkit.capstone.carkirapp.data.remote.network.ApiService
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import com.bangkit.capstone.carkirapp.model.AndroidModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val apiService: ApiService,
    private val placeDao: PlaceDao,
    private val preference: DataPreference
) {

    /** REMOTE SOURCE
     *
     * POST /tokenizable
     * requestBody: { isMobile: true }
     *
     * Get token from server and save to the datastore
     * */
    suspend fun tokenKey() {
        coroutineScope {
            try {
                val response = apiService.getToken(AndroidModel())
                preference.updateToken(response.accessToken)
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    /** REMOTE SOURCE
     *
     * GET /read/allPlaces
     *
     * Get all parking places
     */
    fun getAllParkingPlace(token: String): Flow<Resource<List<PlacesResponseItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllPlace(addPrefixBearer(token))
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.d(TAG, e.message.toString())
        }
    }

    /** REMOTE SOURCE
     *
     * GET /read/{:name}
     *
     * Get detail parking place from server
     * */
    fun getPlaceDetail(token: String, name: String): Flow<Resource<DetailPlaceResponse>> = flow {
        emit(Resource.Loading())
        val dataOnDB = placeDao.getPlace(name).first()
        try {
            val response = apiService.getPlace(addPrefixBearer(token), name)
            response.isFavorite = dataOnDB?.isFavorite ?: false
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.d(TAG, e.message.toString())
        }
    }

    /** REMOTE SOURCE
     *
     * GET /read/Occupancy/{:name}/{:floor}
     *
     * Get occupancy parking place from server
     * */
    fun getOccupancyParkingPlace(
        token: String,
        name: String,
        floor: Int
    ): Flow<Resource<List<OccupancyPlaceResponseItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getOccupancyFloor(addPrefixBearer(token), name, floor)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.d(TAG, e.message.toString())
        }
    }

    /** LOCAL SOURCE
     *
     * Get new token from datastore
     * */
    fun getTokenFromDataStore(): Flow<String> = preference.loadToken()

    /** LOCAL SOURCE
     *
     * Insert data places to database
     * */
    suspend fun insertPlaces(place: PlacesEntity) {
        placeDao.insertPlace(place)
    }

    /** LOCAL SOURCE
     *
     * Update isAlreadySee value on specific data place
     * */
    suspend fun updateHistory(place: PlacesEntity) {
        placeDao.updatePlaces(place)
    }

    /** LOCAL SOURCE
     *
     * Update isFavorite value on specific data place
     * */
    suspend fun updateFavorite(place: PlacesEntity) {
        placeDao.updatePlaces(place)
    }

    /** LOCAL SOURCE
     *
     * Update place data where specific name to change isFavorite value
     */
    suspend fun updateFavoriteDetail(name: String, newState: Boolean) {
        placeDao.updateFavoritePlace(name, newState)
    }

    /** LOCAL SOURCE
     *
     * Update all place where isAlreadySee field have true value to false
     */
    suspend fun deleteHistories() {
        placeDao.removeHistories()
    }

    /** LOCAL SOURCE
     *
     * Get all favorite place from database
     * */
    fun getFavoritesPlace(): Flow<List<PlacesEntity>> = placeDao.getFavorites()

    /** LOCAL SOURCE
     *
     * Get all history place from database
     * */
    fun getHistoriesPlace(): Flow<List<PlacesEntity>> = placeDao.getHistories()

    /** LOCAL SOURCE
     *
     * Get three recent parking places from database
     * */
    fun getRecentPlaces(): Flow<List<PlacesEntity>> = placeDao.getRecentPlaces()

    /**
     * Add prefix "Bearer" to token
     * */
    private fun addPrefixBearer(token: String): String = "Bearer $token"

    companion object {
        const val TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            placeDao: PlaceDao,
            preference: DataPreference
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, placeDao, preference)
            }.also { instance = it }
    }
}