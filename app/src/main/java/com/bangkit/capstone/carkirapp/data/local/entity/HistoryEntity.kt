package com.bangkit.capstone.carkirapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_place")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "total_space")
    var totalSpace: Int,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "insert_at", defaultValue = "CURRENT_TIMESTAMP")
    var insertAt: String
)
