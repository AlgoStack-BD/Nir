package com.algostack.nir.utils

sealed class VerifyCodeNetworkResult <T> (val data: T?= null, val message: String?= null) {

    class Success <T> (data: T) : VerifyCodeNetworkResult<T>(data)
    class Error<T>(message: String?,data: T?=null) : VerifyCodeNetworkResult<T> (data, message)
    class Loading <T> : VerifyCodeNetworkResult<T>()


}