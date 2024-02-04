package com.algostack.nir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.services.repository.ImageUploadRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ImageUploadViewModel @Inject constructor(
    private val repository: ImageUploadRepository
) : ViewModel() {

    val uploadImageResponseLiveData : LiveData<NetworkResult<UploadImageResponse>>
        get() = repository.uploadImageResponseLiveData
    fun addMultipleImages(listImage:  MutableList<File>){


        viewModelScope.launch {
            repository.uploadImage(listImage)
        }
    }

}