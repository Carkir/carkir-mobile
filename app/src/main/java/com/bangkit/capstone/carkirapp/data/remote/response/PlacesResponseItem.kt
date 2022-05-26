package com.bangkit.capstone.carkirapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class PlacesResponseItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("totalEmptySpace")
	val totalEmptySpace: Int,

	@field:SerializedName("status")
	val status: String
)
