package com.algostack.nir.services.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ImageUploadRepository
@Inject constructor(
    private val publicPostApi: PublicPostApi
) {
    private val _uploadImageResponseLiveData = MutableLiveData<NetworkResult<UploadImageResponse>> ()
    val uploadImageResponseLiveData : LiveData<NetworkResult<UploadImageResponse>>
        get() = _uploadImageResponseLiveData
    suspend fun uploadImage(listImage: MutableList<File>) {

        _uploadImageResponseLiveData.postValue(NetworkResult.Loading())

        val parts: List<MultipartBody.Part> = listImage.map { file ->
            MultipartBody.Part.createFormData(
                "files",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        }

        try {



            val response = publicPostApi.uploadImage(parts)

            if (response.isSuccessful && response.body() != null) {
                _uploadImageResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
        } catch (e: Exception) {
            _uploadImageResponseLiveData.postValue(NetworkResult.Error(e.message))
        } catch (e: TimeoutException) {
            _uploadImageResponseLiveData.postValue(NetworkResult.Error("Time Out"))
        }
    }
}