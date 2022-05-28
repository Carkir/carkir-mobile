package com.bangkit.capstone.carkirapp.data

/**
 * Handle response from Remote Source to tracking the state
 * this class have state for Loading, Success and Error.
 * */
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
