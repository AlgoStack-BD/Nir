package com.algostack.nir.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.repository.PublicPostRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PublicPostViewModel @Inject constructor(private val publicPostRepository: PublicPostRepository) : ViewModel() {

    private var disposable = CompositeDisposable()
    val publicPostResponseLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = publicPostRepository.publicPostResponseLiveData


    val createPostResponseLiveData : LiveData<NetworkResult<CreatePostResponse>>
        get() = publicPostRepository.createPostResponseLiveData

    var applicationContext: Context ?= null


   fun publicPost(){
        viewModelScope.launch {
            applicationContext?.let {
                publicPostRepository.publicPost(it)
            }
        }
    }

    fun singleUserPost(){
        viewModelScope.launch {
            applicationContext?.let {
                publicPostRepository.singleUserPost(it)
            }
        }
    }

    fun createPost(createPost: CreatePost){
        viewModelScope.launch {
            applicationContext?.let {
                publicPostRepository.createPost(it,createPost)
            }
        }
    }

    fun addMultipleImages(listImage: ArrayList<Uri>){


       viewModelScope.launch {
           publicPostRepository.uploadImage(listImage)
       }
    }





}