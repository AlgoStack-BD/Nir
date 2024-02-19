package com.algostack.nir.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.BooleanCheck
import com.algostack.nir.services.model.FavouriteRequest
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.RemoveFavouriteItem
import com.algostack.nir.services.repository.FavouriteRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val favouriteRepository: FavouriteRepository) : ViewModel() {

     val favouritePost : LiveData<NetworkResult<FavouriteResponse>>
        get() = favouriteRepository.userFavouretResponse

    val userFavouritePost : LiveData<NetworkResult<PublicPostResponse>>
        get() = favouriteRepository.favouretResponse

    val specificFavouritePost : LiveData<NetworkResult<BooleanCheck>>
        get() = favouriteRepository.specificFavouretResponse




    var applicationContext: Context?= null

    fun createFavorite(favorite: FavouriteRequest){
        viewModelScope.launch {
            applicationContext?.let {
                favouriteRepository.createFavorite(it,favorite)
            }
        }
    }

    fun getUserFavourite(id: String){
        viewModelScope.launch {
            applicationContext?.let {
                favouriteRepository.getUserFavourite(it,id)
            }
        }
    }

    fun updateFavorite(id: String, removeFavouriteItem: RemoveFavouriteItem){
        viewModelScope.launch {
            applicationContext?.let {
                favouriteRepository.updateFavorite(it,id,removeFavouriteItem)
            }
        }
    }

    fun getSpecificFavourite(userId: String, postId: String){
        viewModelScope.launch {
            applicationContext?.let {
                favouriteRepository.getSpecificFavourite(it,userId,postId)
            }
        }
    }

}