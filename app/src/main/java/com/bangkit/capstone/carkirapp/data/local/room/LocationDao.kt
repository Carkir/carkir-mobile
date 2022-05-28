package com.bangkit.capstone.carkirapp.data.local.room

import androidx.room.*
import com.bangkit.capstone.carkirapp.data.local.entity.FavoriteEntity
import com.bangkit.capstone.carkirapp.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * This DAO for operation Favorite and History Entity
 *
 * FavoriteEntity operation: Insert, Delete, GetAll
 * HistoryEntity operation: Insert, Delete, DeleteAll, GetAll
 *
 * */

@Dao
interface LocationDao {

    // Favorite Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(place: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(place: FavoriteEntity)

    @Query("SELECT * FROM favorite_place")
    fun getAllFavorite(): Flow<List<FavoriteEntity>>

    // History Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(place: HistoryEntity)

    @Delete
    suspend fun deleteHistory(place: HistoryEntity)

    @Query("SELECT * FROM history_place ORDER BY insert_at")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history_place ORDER BY insert_at LIMIT 3")
    fun getRecentHistory(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM history_place")
    suspend fun deleteAllHistory()
}