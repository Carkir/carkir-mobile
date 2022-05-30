package com.bangkit.capstone.carkirapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class TokenResponse(

	@field:SerializedName("accessToken")
	val accessToken: String
)
