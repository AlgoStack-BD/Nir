package com.algostack.nir.services.repository

import com.algostack.nir.services.api.UserApi
import javax.inject.Inject

class userRepository @Inject constructor(private val userApi: UserApi) {
}