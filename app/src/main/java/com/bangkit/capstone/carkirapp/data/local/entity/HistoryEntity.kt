package com.bangkit.capstone.carkirapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "history_place")
data class HistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "total_space")
    var totalSpace: Int,

    @ColumnInfo(name = "time")
    var time: String,

    @ColumnInfo(name = "insert_at")
    var insertAt: String = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date())
)
