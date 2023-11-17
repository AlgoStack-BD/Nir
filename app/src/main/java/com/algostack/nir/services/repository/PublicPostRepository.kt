package com.algostack.nir.services.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.model.PublicPostResponse
import com.algostack.nir.services.model.VerificationResponse
import com.algostack.nir.utils.AlertDaialog
import com.algostack.nir.utils.AlertDaialog.noInternetConnectionAlertBox
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import org.json.JSONObject
import retrofit2.Response
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class PublicPostRepository @Inject constructor(private val publicPostApi: PublicPostApi) {

    private val _publicPostResponseLiveData = MutableLiveData<NetworkResult<PublicPostResponse>> ()

    val publicPostResponseLiveData : LiveData<NetworkResult<PublicPostResponse>>
        get() = _publicPostResponseLiveData



    suspend fun publicPost(context: Context){

        if(NetworkUtils.isInternetConnected((context))){
            _publicPostResponseLiveData.postValue(NetworkResult.Loading())


            try {
                val response = publicPostApi.getPublicPost()
                Log.d("TAGNoteResponse", response.body().toString())
                handleNetworkResponse(response)
            }catch (e: Exception){
                _publicPostResponseLiveData.postValue(NetworkResult.Error(e.message))
                println("Exeption201: ${e}")
            }catch (e: TimeoutException){
                _publicPostResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else{

            noInternetConnectionAlertBox(context)

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



}