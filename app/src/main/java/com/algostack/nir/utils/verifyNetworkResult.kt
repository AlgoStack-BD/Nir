package com.algostack.nir.utils

sealed class verifyNetworkResult <T> (val data: T?= null, val message: String?= null) {

    class Success <T> (data: T) : verifyNetworkResult<T>(data)
    class Error<T>(message: String?,data: T?=null) : verifyNetworkResult<T> (data, message)
    class Loading <T> : verifyNetworkResult<T>()


}