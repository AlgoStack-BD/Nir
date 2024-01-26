package com.algostack.nir.services.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.ProfileApi
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi,
) {


    private val _profileInfoResponseLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()

    val profileInfoResponseLiveData : MutableLiveData<NetworkResult<PublicPostResponse>>
        get() = _profileInfoResponseLiveData

    suspend fun singleUserPost(context: Context) {

        if (NetworkUtils.isInternetConnected((context))) {
            _profileInfoResponseLiveData.postValue(NetworkResult.Loading())

            try {
                val response = profileApi.getSingleUserPost()

                if (response.isSuccessful && response.body() != null) {

                    _profileInfoResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
                }
            }catch (e: Exception) {
                _profileInfoResponseLiveData.postValue(NetworkResult.Error(e.message))
                println("Exeption201: ${e}")
            }catch (e: TimeoutException) {
                _profileInfoResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }
}