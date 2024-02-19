package com.algostack.nir.services.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.FavouriteAPI
import com.algostack.nir.services.model.BooleanCheck
import com.algostack.nir.services.model.FavouriteRequest
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.RemoveFavouriteItem
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils.Companion.isInternetConnected
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class FavouriteRepository @Inject constructor(
    private val favouriteAPI: FavouriteAPI
) {
    private val _userFavouretResponse = MutableLiveData<NetworkResult<FavouriteResponse>> ()
    private val _favouretResponse = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    private val _specificFavouretResponse = MutableLiveData<NetworkResult<BooleanCheck>> ()


    val userFavouretResponse : LiveData<NetworkResult<FavouriteResponse>>
        get() = _userFavouretResponse

    val favouretResponse : LiveData<NetworkResult<PublicPostResponse>>
        get() = _favouretResponse

    val specificFavouretResponse : LiveData<NetworkResult<BooleanCheck>>
        get() = _specificFavouretResponse



    suspend fun createFavorite(context: Context, favorite: FavouriteRequest) {

        if (isInternetConnected((context))) {
            _userFavouretResponse.postValue(NetworkResult.Loading())

            try {
                val response = favouriteAPI.createFavorite(favorite)

                if (response.isSuccessful && response.body() != null) {

                    _userFavouretResponse.postValue(NetworkResult.Success(response.body()!!))

                    println("CheckResponse: ${response.body()}")
                }
            }catch (e: Exception) {
                _userFavouretResponse.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _userFavouretResponse.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }


    suspend fun getUserFavourite(context: Context, id: String) {
        if (isInternetConnected((context))) {
            _favouretResponse.postValue(NetworkResult.Loading())

            try {
                val response = favouriteAPI.getUserFavourite(id)

                if (response.isSuccessful && response.body() != null) {

                    _favouretResponse.postValue(NetworkResult.Success(response.body()!!))

                    println("CheckResponse: ${response.body()}")
                }
            }catch (e: Exception) {
                _favouretResponse.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _favouretResponse.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }

    suspend fun updateFavorite(context: Context, id: String, removeFavouriteItem: RemoveFavouriteItem) {
        if (isInternetConnected((context))) {
            _userFavouretResponse.postValue(NetworkResult.Loading())

            try {
                val response = favouriteAPI.updateFavorite(id, removeFavouriteItem)

                if (response.isSuccessful && response.body() != null) {

                    _userFavouretResponse.postValue(NetworkResult.Success(response.body()!!))

                    println("CheckResponse: ${response.body()}")
                }
            }catch (e: Exception) {
                _userFavouretResponse.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _userFavouretResponse.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }

    suspend fun getSpecificFavourite(context: Context, userId: String, postId: String) {
        if (isInternetConnected((context))) {
            _specificFavouretResponse.postValue(NetworkResult.Loading())

            try {
                val response = favouriteAPI.getSpecificFavourite(userId, postId)

                if (response.isSuccessful && response.body() != null) {

                    _specificFavouretResponse.postValue(NetworkResult.Success(response.body()!!))

                    println("CheckResponse: ${response.body()}")
                }
            }catch (e: Exception) {
                _specificFavouretResponse.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _specificFavouretResponse.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }



}

