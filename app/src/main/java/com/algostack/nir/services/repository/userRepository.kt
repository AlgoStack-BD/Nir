package com.algostack.nir.services.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.nir.services.api.UserApi
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.services.db.NirLocalDB
import com.algostack.nir.services.model.ForgetPasswordRequest
import com.algostack.nir.services.model.SignupResponse
import com.algostack.nir.services.model.UpdateStatusRequest
import com.algostack.nir.services.model.UserRequest
import com.algostack.nir.services.model.UserResponse
import com.algostack.nir.services.model.UserSigninRequest
import com.algostack.nir.services.model.UserUpdateRequest
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.services.model.VerificationResponse
import com.algostack.nir.services.model.VerifyOTPResponse
import com.algostack.nir.services.model.VerifyRequest
import com.algostack.nir.services.model.userUpdateRequestResponse
import com.algostack.nir.utils.AlertDaialog.noInternetConnectionAlertBox
import com.algostack.nir.utils.Constants
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.NetworkUtils
import okio.IOException
import org.json.JSONObject
import retrofit2.Response
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class userRepository @Inject constructor(
    private val userApi: UserApi,
    private  val verificationAPI: VerificationAPI,
    private val nirLocalDB: NirLocalDB

) {


    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    private val _signupRespnseLiveData = MutableLiveData<NetworkResult<SignupResponse>>()
    private val _verifyResponseLiveData = MutableLiveData<NetworkResult<VerificationResponse>>()
    private val _VerifyOtpMessageLiveData = MutableLiveData<NetworkResult<VerifyOTPResponse>>()

    private val _userUpdateResponseLiveData = MutableLiveData<NetworkResult<userUpdateRequestResponse>>()


    val verificationResponse: LiveData<NetworkResult<VerificationResponse>>
        get() = _verifyResponseLiveData

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    val verifyOtpMessageLiveData: LiveData<NetworkResult<VerifyOTPResponse>>
        get() = _VerifyOtpMessageLiveData


    val requestResponseLiveData: LiveData<NetworkResult<userUpdateRequestResponse>>
        get() = _userUpdateResponseLiveData


    val signupRespnseLiveData: LiveData<NetworkResult<SignupResponse>>
        get() = _signupRespnseLiveData

    suspend fun registerUser(userRequest: UserRequest , context: Context) {

        if (NetworkUtils.isInternetConnected(context)) {
            _signupRespnseLiveData.postValue(NetworkResult.Loading())
            try {
                val response = userApi.signup(userRequest)
                Log.d(Constants.TAG, response.body().toString())
                if (response.isSuccessful && response.body() != null){




                    _signupRespnseLiveData.postValue(NetworkResult.Success(response.body()!!))
                }else if (response.errorBody() != null){
                    val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                    _signupRespnseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
                }else{
                    _signupRespnseLiveData.postValue(NetworkResult.Error("Something went wrong"))
                }
            } catch (e: Exception) {
                _signupRespnseLiveData.postValue(NetworkResult.Error(e.message))
            }  catch (e: TimeoutException) {
                _signupRespnseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else{

            noInternetConnectionAlertBox(context)

        }


    }

    suspend fun loginUser(userSigninRequest: UserSigninRequest, context: Context) {




        if (NetworkUtils.isInternetConnected(context)) {
          //  println("Testapicall: ${userApi.signin(userSigninRequest)}")
            _userResponseLiveData.postValue(NetworkResult.Loading())
            try {
                val response = userApi.signin(userSigninRequest)

                println("Testapicall: ${response}")

               Log.d(Constants.TAG, response.body().toString())
//
//                if (response.isSuccessful && response.body() != null){
//                    nirLocalDB.getLoginInfo().upsert(response.body()!!.data)
//                }

                handleNetworkResponseU(response)
            } catch (e: Exception) {
                _userResponseLiveData.postValue(NetworkResult.Error(e.message))
            } catch (e: TimeoutException) {
                _userResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else{


            noInternetConnectionAlertBox(context)
        }

    }

    suspend fun verification(verificationRequest: VerificationRequest, context: Context) {

        if (NetworkUtils.isInternetConnected(context)) {
            _verifyResponseLiveData.postValue(NetworkResult.Loading())
            try {
                val response = verificationAPI.verification(verificationRequest)
                Log.d(Constants.TAG, response.body().toString())
                handleNetworkResponseV(response)
            } catch (e: Exception) {
                _verifyResponseLiveData.postValue(NetworkResult.Error(e.message))
            }catch (e: TimeoutException) {
                _verifyResponseLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else
        {
            noInternetConnectionAlertBox(context)

        }

    }

    suspend fun verifycode(verifyOtpRequest: VerifyRequest, context: Context) {


        if (NetworkUtils.isInternetConnected(context)) {
            _VerifyOtpMessageLiveData.postValue(NetworkResult.Loading())

            try {
                val response = verificationAPI.vedifyOTP(verifyOtpRequest)
                Log.d(Constants.TAG, response.body().toString())
                handleNetworkResponseVC(response)
            } catch (e: Exception) {
                _VerifyOtpMessageLiveData.postValue(NetworkResult.Error(e.message))
            }  catch (e: TimeoutException) {
                _VerifyOtpMessageLiveData.postValue(NetworkResult.Error("Time Out"))
            }
        }else{
            noInternetConnectionAlertBox(context)
        }

    }

    suspend fun UpdateStatus(id: String, updateStatusRequest: UpdateStatusRequest, context: Context) {
        if (NetworkUtils.isInternetConnected(context)) {
            val response = verificationAPI.upVerifyStatus(id, updateStatusRequest)
            Log.d(Constants.TAG, response.body().toString())
        }

    }

    suspend fun ResetPassword(forgetPasswordRequest: ForgetPasswordRequest , context: Context) {
        if (NetworkUtils.isInternetConnected(context)) {
            val response = verificationAPI.ResetPassword(forgetPasswordRequest)
            Log.d(Constants.TAG2, response.body().toString())
            handleNetworkResponseU(response)
        }
    }



    suspend fun updateUserInfo(_id: String, userUpdateRequest: UserUpdateRequest) {
        println("Testapicall1: $_id")
        println("Testapicall2: $userUpdateRequest")
        _userUpdateResponseLiveData.postValue(NetworkResult.Loading())

        try {
            val response = userApi.updateUserInfo(_id, userUpdateRequest)

            if (response.isSuccessful && response.body() != null) {
                _userUpdateResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                _userUpdateResponseLiveData.postValue(NetworkResult.Error(errorMessage))
            }
        } catch (e: IOException) {
            _userUpdateResponseLiveData.postValue(NetworkResult.Error("Network error: ${e.message}"))
        } catch (e: Exception) {
            _userUpdateResponseLiveData.postValue(NetworkResult.Error("Error: ${e.message}"))
            println("TestapicallErrror: ${e.message}")
        }
    }



    private suspend fun handleNetworkResponseU(response: Response<UserResponse>){
        if (response.isSuccessful && response.body() != null){




            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }


    private fun handleNetworkResponseV(response: Response<VerificationResponse>){
        if (response.isSuccessful && response.body() != null){
            _verifyResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _verifyResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _verifyResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
    private fun handleNetworkResponseVC(response: Response<VerifyOTPResponse>){
        if (response.isSuccessful && response.body() != null){
            _VerifyOtpMessageLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if (response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _VerifyOtpMessageLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _VerifyOtpMessageLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }





}


