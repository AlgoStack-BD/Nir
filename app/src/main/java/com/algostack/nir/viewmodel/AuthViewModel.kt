package com.algostack.nir.viewmodel

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.nir.services.model.ForgetPasswordRequest
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
import com.algostack.nir.services.repository.userRepository
import com.algostack.nir.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: userRepository) : ViewModel() {



    val userResponeLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    val VerifyOtpMessageLiveData : LiveData<NetworkResult<VerifyOTPResponse>>
        get() = userRepository.verifyOtpMessageLiveData

    val VerifyResponseLiveData : LiveData<NetworkResult<VerificationResponse>>
        get() = userRepository.verificationResponse

    val userUpdateResponseLiveData : LiveData<NetworkResult<userUpdateRequestResponse>>
        get() = userRepository.requestResponseLiveData

    var applicationContext: Context? = null


    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.registerUser(userRequest, it) }
        }

    }

    fun loginUser(userSigninRequest: UserSigninRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.loginUser(userSigninRequest, it) }
        }
    }

    fun VerificationRequest(verificationRequest: VerificationRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.verification(verificationRequest, it) }
        }
    }
    fun verifyOTP(verifyOtpRequest : VerifyRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.verifycode(verifyOtpRequest, it) }
        }
    }

    fun updateVerifyStatus(id: String, updateStatusRequest: UpdateStatusRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.UpdateStatus(id, updateStatusRequest, it) }
        }
    }

    fun ResetPassword(forgetPasswordRequest: ForgetPasswordRequest){
        viewModelScope.launch {
            applicationContext?.let { userRepository.ResetPassword(forgetPasswordRequest, it) }
        }
    }

    fun validateCredintials(username: String,emailAdress: String,password: String,confirmPass: String,isLogin: Boolean) :Pair<Boolean,String>{



//        val validEmailRegex = Regex("^(?=.*[@])(?=\\S+$).{6,}\$")
//        val validEmailDomains = listOf("gmail.com", "yahoo.com")
        var result = Pair(true,"")
        if ((!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(emailAdress) || TextUtils.isEmpty(password) || (!isLogin && TextUtils.isEmpty(confirmPass))){
            result = Pair(false,"Please provide User Name")
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailAdress).matches()){
            result = Pair(false, "Please provide valid email")
        }


        return result
    }



    fun updateUserInfo(id:String, updateRequest: UserUpdateRequest){
        println("updateUserInfo called: $id, $updateRequest")
        viewModelScope.launch {
           userRepository.updateUserInfo(id,updateRequest)
        }
    }
}