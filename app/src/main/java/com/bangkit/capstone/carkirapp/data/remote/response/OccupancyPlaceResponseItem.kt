package com.bangkit.capstone.carkirapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class OccupancyPlaceResponseItem(

	@field:SerializedName("Floor")
	val floor: Int,

	@field:SerializedName("Occupancy")
	val occupancy: Int,

	@field:SerializedName("Cluster")
	val cluster: String,

	@field:SerializedName("Slot")
	val slot: Int,

	@field:SerializedName("floorAvailability")
	val floorAvailability: Int
)
