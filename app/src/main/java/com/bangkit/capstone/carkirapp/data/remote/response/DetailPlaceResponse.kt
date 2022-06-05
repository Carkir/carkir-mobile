package com.bangkit.capstone.carkirapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailPlaceResponse(

	@field:SerializedName("priceLow")
	val priceLow: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("clusterCount")
	val clusterCount: List<String>,

	@field:SerializedName("time")
	val time: String,

	@field:SerializedName("totalEmptySpace")
	val totalEmptySpace: Int,

	@field:SerializedName("priceHigh")
	val priceHigh: String,

	@field:SerializedName("status")
	val status: String,

	@field:SerializedName("alamat")
	val address: String,

	@field:SerializedName("image")
	val image: String?,

	var isFavorite: Boolean,
)
