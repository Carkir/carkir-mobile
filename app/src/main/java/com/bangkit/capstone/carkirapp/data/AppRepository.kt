package com.bangkit.capstone.carkirapp.data

import android.util.Log
import com.bangkit.capstone.carkirapp.data.local.datastore.TokenPreference
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import com.bangkit.capstone.carkirapp.data.local.room.LocationDao
import com.bangkit.capstone.carkirapp.data.remote.network.ApiService
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.data.remote.response.OccupancyPlaceResponseItem
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import com.bangkit.capstone.carkirapp.model.AndroidModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val apiService: ApiService,
    private val locationDao: LocationDao,
    private val preference: TokenPreference
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
            val response = apiService.getToken(AndroidModel())
            preference.updateToken(response.accessToken)
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
    fun getParkingPlace(token: String, name: String): Flow<Resource<DetailPlaceResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getPlace(addPrefixBearer(token), name)
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
     * Get three recent parking places from database
     * */
    fun getRecentParkingPlace(): Flow<List<HistoryEntity>> = locationDao.getRecentHistory()

    /** LOCAL SOURCE
     *
     * Get all favorite place from database
     * */
    fun getAllFavoritePlaces(): Flow<List<FavoriteEntity>> = locationDao.getAllFavorite()

    /** LOCAL SOURCE
     *
     * Add favorite place to database
     * */
    suspend fun insertFavoritePlace(place: FavoriteEntity) {
        locationDao.insertFavorite(place)
    }

    /** LOCAL SOURCE
     *
     * Delete favorite place on database
     * */
    suspend fun deleteFavoritePlace(place: FavoriteEntity) {
        locationDao.deleteFavorite(place)
    }

    /** LOCAL SOURCE
     *
     * Insert history place to database
     * */
    suspend fun addPlaceToHistory(place: HistoryEntity) {
        locationDao.insertHistory(place)
    }

    /** LOCAL SOURCE
     *
     * Get all history place from database
     * */
    fun getAllHistoryPlaces(): Flow<List<HistoryEntity>> = locationDao.getAllHistory()

    /** LOCAL SOURCE
     *
     * Delete one history on database
     * */
    suspend fun deleteHistoryPlace(place: HistoryEntity) {
        locationDao.deleteHistory(place)
    }

    /** LOCAL SOURCE
     *
     * Delete all history on database
     */
    suspend fun deleteAllHistory() {
        locationDao.deleteAllHistory()
    }

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
            locationDao: LocationDao,
            preference: TokenPreference
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, locationDao, preference)
            }.also { instance = it }
    }
}