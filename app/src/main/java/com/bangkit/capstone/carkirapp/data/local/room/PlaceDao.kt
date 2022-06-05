package com.bangkit.capstone.carkirapp.data.local.room

import androidx.room.*
import com.bangkit.capstone.carkirapp.data.local.entity.PlacesEntity
import kotlinx.coroutines.flow.Flow

/**
 * This DAO for operation Save and Updating Parking place, also for tracking Favorite and History
 *
 * How the tracking favorite and history parking place work?
 * the idea or logic is simple just changing or updating state to true (1) or false (0) in specific field.
 *
 * Updating state place for favorite: change field isFavorite to true (1) or false (0)
 * Updating state place for history: change field isAlreadySee to true (1) or false (0)
 * */

@Dao
interface PlaceDao {

    // Place Operations
    @Query("SELECT * FROM places WHERE isFavorite = 1 ORDER BY name")
    fun getFavorites(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM places WHERE isAlreadySee = 1 ORDER BY insertAt DESC")
    fun getHistories(): Flow<List<PlacesEntity>>

    @Query("SELECT * FROM places WHERE isAlreadySee = 1 ORDER BY insertAt DESC LIMIT 5")
    fun getRecentPlaces(): Flow<List<PlacesEntity>>

    @Query("UPDATE places SET isAlreadySee = 0 WHERE isAlreadySee = 1")
    suspend fun removeHistories()

    @Query("SELECT * FROM places WHERE name = :name LIMIT 1")
    fun getPlace(name: String): Flow<PlacesEntity?>

    @Query("UPDATE places SET isFavorite = :newState WHERE name = :name")
    suspend fun updateFavoritePlace(name: String, newState: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(place: PlacesEntity)

    @Update
    suspend fun updatePlaces(place: PlacesEntity)
}