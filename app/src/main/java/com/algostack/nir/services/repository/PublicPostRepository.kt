package com.algostack.nir.services.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.utils.AlertDaialog.noInternetConnectionAlertBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import com.algostack.nir.utils.NetworkUtils.Companion.isInternetConnected
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class PublicPostRepository @Inject constructor(
    private val publicPostApi: PublicPostApi,
    private val nirLocalDB: NirLocalDB

) {

    private val _publicPostResponseLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    private  val _createPostResponseLiveData = MutableLiveData<NetworkResult<CreatePostResponse>> ()
    private val _uploadImageResponseLiveData = MutableLiveData<NetworkResult<UploadImageResponse>> ()
    val publicPostResponseLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = _publicPostResponseLiveData

    val createPostResponseLiveData : LiveData<NetworkResult<CreatePostResponse>>
        get() = _createPostResponseLiveData

    val uploadImageResponseLiveData : LiveData<NetworkResult<UploadImageResponse>>
        get() = _uploadImageResponseLiveData



    suspend fun publicPost(context: Context){

        if(NetworkUtils.isInternetConnected((context))){
            _publicPostResponseLiveData.postValue(NetworkResult.Loading())

            try {
                val response = publicPostApi.getPublicPost()

                if (response.isSuccessful && response.body() != null){

                    nirLocalDB.getPublicPostDao().insertPublicPost(response.body()!!.data)


                }


                handleNetworkResponse(response)
            }catch (e: Exception){
                _publicPostResponseLiveData.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException){
                _publicPostResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else{

            withContext(Dispatchers.IO){
                val publicPostData = nirLocalDB.getPublicPostDao().getPublicPostData()

                if (publicPostData!!.isNotEmpty()){
                    _publicPostResponseLiveData.postValue(NetworkResult.Success(PublicPostResponse(publicPostData,200)))
                }else{
                    noInternetConnectionAlertBox(context)
                }
            }



        }

    }

    suspend fun createPost(context: Context,createPost: CreatePost) {

        if (isInternetConnected((context))) {
            _createPostResponseLiveData.postValue(NetworkResult.Loading())

            try {
                val response = publicPostApi.createPost(createPost)

                if (response.isSuccessful && response.body() != null) {

                    _createPostResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

                }
            }catch (e: Exception) {
                _createPostResponseLiveData.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _createPostResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }




    private fun handleNetworkResponse(response: Response<PublicPostResponse>) {

        if(response.isSuccessful && response.body() != null){
            println("CheckResponse: ${response.body()}")

            _publicPostResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody() != null){
            val erroObj = JSONObject(response.errorBody()!!.charStream().readText())
            _publicPostResponseLiveData.postValue(NetworkResult.Error(erroObj.getString("message")))
        }else{
            _publicPostResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }

    suspend fun uploadImage(listImage: ArrayList<Uri>) {
        listImage.forEach{
            println("CheckUri2: = $it")
        }
        _createPostResponseLiveData.postValue(NetworkResult.Loading())

        try {

            val images = listImage.map { imageUri->
                val file = imageUri.path?.let { File(it) }
                val requestBody = file!!.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("files",file.name,requestBody)
            }

            val response = publicPostApi.uploadImage(images)
             println("Chekabd: "+ response.body())
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