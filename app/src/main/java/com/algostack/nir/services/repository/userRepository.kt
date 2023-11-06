package com.algostack.nir.services.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.UserApi
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.services.model.ForgetPasswordRequest
import com.algostack.nir.services.model.UpdateStatusRequest
import com.algostack.nir.services.model.UserLoginRequest
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.services.model.VerificationResponse
import com.algostack.nir.services.model.VerifyOTPResponse
import com.algostack.nir.services.model.VerifyRequest
import com.algostack.nir.utils.Constants
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.PostResult
import com.algostack.nir.utils.VerifyCodeNetworkResult
import com.algostack.nir.utils.verifyNetworkResult
import okio.IOException
import org.json.JSONObject
import retrofit2.Response
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class userRepository @Inject constructor(private val userApi: UserApi,private  val verificationAPI: VerificationAPI) {



    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>> ()
    private val _verifyResponseLiveData = MutableLiveData<verifyNetworkResult<VerificationResponse>>()
    private val _VerifyOtpMessageLiveData = MutableLiveData<VerifyCodeNetworkResult<VerifyOTPResponse>>()

    val verificationResponse : LiveData<verifyNetworkResult<VerificationResponse>>
        get() = _verifyResponseLiveData

    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    val verifyOtpMessageLiveData : LiveData<VerifyCodeNetworkResult<VerifyOTPResponse>>
        get() =  _VerifyOtpMessageLiveData

    suspend fun registerUser(userRequest: UserRequest){

        _userResponseLiveData.postValue(NetworkResult.Loading())
        try {
            val response = userApi.signup(userRequest)
            Log.d(Constants.TAG, response.body().toString())
            handlerResponse(response)
        }catch (e: Exception){
            _userResponseLiveData.postValue(NetworkResult.Error(e.message))
        }catch (e: IOException){
            _userResponseLiveData.postValue(NetworkResult.Error("Network Error"))
        }catch (e: TimeoutException){
            _userResponseLiveData.postValue(NetworkResult.Error("Time Out"))
        }



    }

    suspend fun loginUser(userSigninRequest: UserSigninRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())

        try {
            val response = userApi.signin(userSigninRequest)

            Log.d(Constants.TAG, response.body().toString())
            handlerResponse(response)
        }catch (e: Exception){
            _userResponseLiveData.postValue(NetworkResult.Error(e.message))
        }catch (e: IOException){
            _userResponseLiveData.postValue(NetworkResult.Error("Network Error"))
        }catch (e: TimeoutException){
            _userResponseLiveData.postValue(NetworkResult.Error("Time Out"))
        }

    }

    suspend fun  verification(verificationRequest: VerificationRequest){
        _verifyResponseLiveData.postValue(verifyNetworkResult.Loading())
        try {
            val response = verificationAPI.verification(verificationRequest)
            Log.d(Constants.TAG, response.body().toString())
            handlerResponseV(response)
        }catch (e: Exception){
            _verifyResponseLiveData.postValue(verifyNetworkResult.Error(e.message))
        }catch (e: IOException){
            _verifyResponseLiveData.postValue(verifyNetworkResult.Error("Network Error"))
        }catch (e: TimeoutException){
            _verifyResponseLiveData.postValue(verifyNetworkResult.Error("Time Out"))
        }

    }

    suspend fun  verifycode(verifyOtpRequest: VerifyRequest){
        _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Loading())
        try {
            val response = verificationAPI.vedifyOTP(verifyOtpRequest)
            Log.d(Constants.TAG, response.body().toString())
            handlerVerifycodeResponse(response)
        }catch (e: Exception){
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Error(e.message))
        }catch (e: IOException){
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Error("Network Error"))
        }catch (e: TimeoutException){
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Error("Time Out"))
        }

    }

    suspend fun UpdateStatus(id: String, updateStatusRequest: UpdateStatusRequest){
        val response = verificationAPI.upVerifyStatus(id,updateStatusRequest)
        Log.d(Constants.TAG, response.body().toString())

    }

    suspend fun ResetPassword(forgetPasswordRequest: ForgetPasswordRequest){
        val response = verificationAPI.ResetPassword(forgetPasswordRequest)
        Log.d(Constants.TAG2, response.body().toString())
        handlerResponse(response)
    }


    private fun handlerResponse(response: Response<UserResponse>){
        if (response.isSuccessful && response.body() != null){
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    private fun handlerResponseV(response: Response<VerificationResponse>){
        if (response.isSuccessful && response.body() != null){
            _verifyResponseLiveData.postValue(verifyNetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _verifyResponseLiveData.postValue(verifyNetworkResult.Error(errorObj.getString("message")))
        }else{
            _verifyResponseLiveData.postValue(verifyNetworkResult.Error("Something went wrong"))
        }
    }

    private fun handlerVerifycodeResponse(response: Response<VerifyOTPResponse>){
        if (response.isSuccessful && response.body() != null){
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Error(errorObj.getString("message")))
        }else{
            _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Error("Something went wrong"))
        }
    }

}