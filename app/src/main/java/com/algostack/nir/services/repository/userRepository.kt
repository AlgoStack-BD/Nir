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
import com.algostack.nir.utils.VerifyCodeNetworkResult
import com.algostack.nir.utils.verifyNetworkResult
import org.json.JSONObject
import retrofit2.Response
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
        val response = userApi.signup(userRequest)
        Log.d(Constants.TAG, response.body().toString())
        handlerResponse(response)


    }

    suspend fun loginUser(userSigninRequest: UserSigninRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())

        val response = userApi.signin(userSigninRequest)

        Log.d(Constants.TAG, response.body().toString())
        handlerResponse(response)
    }

    suspend fun  verification(verificationRequest: VerificationRequest){
        _verifyResponseLiveData.postValue(verifyNetworkResult.Loading())
        val response = verificationAPI.verification(verificationRequest)
        Log.d(Constants.TAG, response.body().toString())
        handlerResponseV(response)
    }

    suspend fun  verifycode(verifyOtpRequest: VerifyRequest){
        _VerifyOtpMessageLiveData.postValue(VerifyCodeNetworkResult.Loading())
        val response = verificationAPI.vedifyOTP(verifyOtpRequest)
        Log.d(Constants.TAG, response.body().toString())
        handlerVerifycodeResponse(response)
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