package com.algostack.nir.services.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.ProfileApi
import com.algostack.nir.services.api.UserApi
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.DeletePostResponseData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.deletePostResponse
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi,
) {



    private val _profileInfoResponseLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    private val _deletePostResponseLiveData = MutableLiveData<NetworkResult<deletePostResponse>> ()

    val profileInfoResponseLiveData : MutableLiveData<NetworkResult<PublicPostResponse>>
        get() = _profileInfoResponseLiveData

    val deletePostResponseLiveData : LiveData<NetworkResult<deletePostResponse>>
        get() = _deletePostResponseLiveData

    suspend fun singleUserPost(context: Context,userID: String) {
        println("functioncalled")

        if (NetworkUtils.isInternetConnected((context))) {
            println("functioncalled2")
            _profileInfoResponseLiveData.postValue(NetworkResult.Loading())

            try {
                val response = profileApi.getSingleUserPost(userID)
                println("testresponse: ${response.body()}")

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


    suspend fun deletePost(id: String) {


        _deletePostResponseLiveData.postValue(NetworkResult.Loading())

        try {
            val response = profileApi.deletePost(id)

            if (response.isSuccessful && response.body() != null) {

                _deletePostResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

            }
        }catch (e: Exception) {
            _deletePostResponseLiveData.postValue(NetworkResult.Error(e.message))

        }catch (e: TimeoutException) {
            _deletePostResponseLiveData.postValue(NetworkResult.Error("Time Out"))
        }

    }



}

