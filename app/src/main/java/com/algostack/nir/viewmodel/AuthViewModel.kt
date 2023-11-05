package com.algostack.nir.viewmodel

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
import com.algostack.nir.services.model.VerificationRequest
import com.algostack.nir.services.model.VerifyOTPResponse
import com.algostack.nir.services.model.VerifyRequest
import com.algostack.nir.services.repository.userRepository
import com.algostack.nir.utils.NetworkResult
import com.algostack.nir.utils.VerifyCodeNetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: userRepository) : ViewModel() {



    val userResponeLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    val VerifyOtpMessageLiveData : LiveData<VerifyCodeNetworkResult<VerifyOTPResponse>>
        get() = userRepository.verifyOtpMessageLiveData


    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userSigninRequest: UserSigninRequest){
        viewModelScope.launch {
            userRepository.loginUser(userSigninRequest)
        }
    }

    fun VerificationRequest(verificationRequest: VerificationRequest){
        viewModelScope.launch {
            userRepository.verification(verificationRequest)
        }
    }
    fun verifyOTP(verifyOtpRequest : VerifyRequest){
        viewModelScope.launch {
            userRepository.verifycode(verifyOtpRequest)
        }
    }

    fun updateVerifyStatus(id: String, updateStatusRequest: UpdateStatusRequest){
        viewModelScope.launch {
            userRepository.UpdateStatus(id, updateStatusRequest)
        }
    }

    fun ResetPassword(forgetPasswordRequest: ForgetPasswordRequest){
        viewModelScope.launch {
            userRepository.ResetPassword(forgetPasswordRequest)
        }
    }

    fun validateCredintials(username: String,emailAdress: String,password: String,confirmPass: String,isLogin: Boolean) :Pair<Boolean,String>{

        println("checkemail: $emailAdress checkname: $username checkpas: $password")


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

}