package com.bangkit.capstone.carkirapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "places")
data class PlacesEntity(
    @PrimaryKey
    val name: String,
    val time: String,
    val status: String,
    val address: String,
    val priceHigh: String,
    val priceLow: String,
    val totalSpace: Int,
    var isAlreadySee: Boolean = false, // Tracking for history
    var isFavorite: Boolean = false, // Tracking for favorite
    var insertAt: String = SimpleDateFormat("dd MMMM yyyy HH:mm:ss").format(Date()),
    val image: String? = null,
)
