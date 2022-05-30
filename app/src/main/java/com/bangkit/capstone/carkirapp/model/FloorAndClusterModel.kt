package com.bangkit.capstone.carkirapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FloorAndClusterModel(
    val namePlace: String,
    val floor: Int,
    val rangeClusters: String,
    val parkingSpace: Int
) : Parcelable