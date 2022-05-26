package com.bangkit.capstone.carkirapp.data.remote.network

import com.bangkit.capstone.carkirapp.data.remote.response.DetailPlaceResponse
import com.bangkit.capstone.carkirapp.data.remote.response.PlacesResponseItem
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("read/allPlace")
    suspend fun getAllPlace(): List<PlacesResponseItem>

    @GET("read/{name}")
    suspend fun getPlace(
        @Path("name") name: String
    ) : DetailPlaceResponse

    // TODO Complete the response type
    @GET("read/Occupancy/{name}/{floor}")
    suspend fun getOccupancyFloor(
        @Path("name") name: String,
        @Path("floor") floor: Int
    )
}