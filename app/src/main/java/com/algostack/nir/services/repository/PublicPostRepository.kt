package com.algostack.nir.services.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.CreatePost
import com.algostack.nir.services.model.CreatePostResponse
import com.algostack.nir.services.model.DeletePostResponseData
import com.algostack.nir.services.model.FavouriteRequest
import com.algostack.nir.services.model.FavouriteResponse
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.UploadImageResponse
import com.algostack.nir.services.model.userPostSoldFieldUpdate
import com.algostack.nir.services.model.userUpdateRequestResponse
import com.algostack.nir.utils.AlertDaialog.noInternetConnectionAlertBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import com.algostack.nir.utils.NetworkUtils.Companion.isInternetConnected
import com.algostack.nir.view.frame.Favorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
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
    private val _nearestPostResponeLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()
    private val _postSoldFieldUpdateResponseLiveData = MutableLiveData<NetworkResult<userUpdateRequestResponse>> ()


    val publicPostResponseLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = _publicPostResponseLiveData

    val createPostResponseLiveData : LiveData<NetworkResult<CreatePostResponse>>
        get() = _createPostResponseLiveData

    val nearestPostResponeLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = _nearestPostResponeLiveData

    val postSoldFieldUpdateResponseLiveData : LiveData<NetworkResult<userUpdateRequestResponse>>
        get() = _postSoldFieldUpdateResponseLiveData







    suspend fun publicPost(context: Context){

       // i want if room db not null then show data from room db and if room db is null then show data from api
        if (nirLocalDB.getPublicPostDao().getPublicPostData() != null){
            _publicPostResponseLiveData.postValue(NetworkResult.Success(PublicPostResponse(nirLocalDB.getPublicPostDao().getPublicPostData()!!,200)))

            try {
                val response = publicPostApi.getPublicPost()

                if (response.isSuccessful && response.body() != null){
                    nirLocalDB.getPublicPostDao().deleteAllPublicPostData()

                    nirLocalDB.getPublicPostDao().insertPublicPost(response.body()!!.data)

                }


                handleNetworkResponse(response)
            }catch (e: Exception){
                _publicPostResponseLiveData.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException){
                _publicPostResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }

        }

        if(NetworkUtils.isInternetConnected((context))){
            _publicPostResponseLiveData.postValue(NetworkResult.Loading())


            try {
                val response = publicPostApi.getPublicPost()

                if (response.isSuccessful && response.body() != null){
                    nirLocalDB.getPublicPostDao().deleteAllPublicPostData()

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

    suspend fun publicnearestPost(context: Context,place : String){





       if(NetworkUtils.isInternetConnected((context))){
            _nearestPostResponeLiveData.postValue(NetworkResult.Loading())


            try {
                println("place: $place")
                val response = publicPostApi.getNearestPost(place)

                if(response.isSuccessful && response.body() != null){
                    println("CheckResponse: ${response.body()}")

                    _nearestPostResponeLiveData.postValue(NetworkResult.Success(response.body()!!))
                }else if(response.errorBody() != null){
                    val erroObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _nearestPostResponeLiveData.postValue(NetworkResult.Error(erroObj.getString("message")))
                }else{
                    _nearestPostResponeLiveData.postValue(NetworkResult.Error("Something went wrong"))
                }

            }catch (e: Exception){
                _nearestPostResponeLiveData.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException){
                _nearestPostResponeLiveData.postValue(NetworkResult.Error("Time Out"))
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



    suspend fun postSoldFiledUpdate(context: Context,postId: String,userPostSoldFieldUpdate : userPostSoldFieldUpdate){

        if (isInternetConnected((context))) {
            _postSoldFieldUpdateResponseLiveData.postValue(NetworkResult.Loading())

            try {
                val response = publicPostApi.updateSoldFiled(postId,userPostSoldFieldUpdate)

                println("CheckResponsesoldfield: ${response}")

                if (response.isSuccessful && response.body() != null) {

                    _postSoldFieldUpdateResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

                }
            }catch (e: Exception) {
                _postSoldFieldUpdateResponseLiveData.postValue(NetworkResult.Error(e.message))

            }catch (e: TimeoutException) {
                _postSoldFieldUpdateResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }
    }



    private fun handleNetworkResponse(response: Response<PublicPostResponse>) {

        if(response.isSuccessful && response.body() != null){


            _publicPostResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody() != null){
            val erroObj = JSONObject(response.errorBody()!!.charStream().readText())
            _publicPostResponseLiveData.postValue(NetworkResult.Error(erroObj.getString("message")))
        }else{
            _publicPostResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }

    }





}