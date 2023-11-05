package com.algostack.nir.utils

sealed class PostResult<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T) : PostResult<T>(data)
    class Error<T>(message: String?, data: T? = null) : PostResult<T>(data, message)
    class Loading<T> : PostResult<T>()
}
