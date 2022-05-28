package com.bangkit.capstone.carkirapp.data

import android.util.Log
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import com.bangkit.capstone.carkirapp.data.local.room.LocationDao
import com.bangkit.capstone.carkirapp.data.remote.network.ApiService
import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppRepository(
    private val apiService: ApiService,
    private val locationDao: LocationDao
) {

    /** REMOTE SOURCE
     *
     * GET /read/allPlaces
     *
     * Get all parking places
     */
    fun getAllParkingPlace(): Flow<Resource<List<PlacesResponseItem>>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getAllPlace()
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
    fun getParkingPlace(name: String): Flow<Resource<DetailPlaceResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = apiService.getPlace(name)
            emit(Resource.Success(response))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            Log.d(TAG, e.message.toString())
        }
    }

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

    companion object {
        const val TAG = "AppRepository"

        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            locationDao: LocationDao
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, locationDao)
            }.also { instance = it }
    }
}