package com.algostack.nir.utils

import android.content.Context
import com.algostack.nir.utils.Constants.PREFS_TOKEN_FILE
import com.algostack.nir.utils.Constants.USER_EMAIL
import com.algostack.nir.utils.Constants.USER_ID
import com.algostack.nir.utils.Constants.USER_IMAGE
import com.algostack.nir.utils.Constants.USER_NAME
import com.algostack.nir.utils.Constants.USER_NUMER
import com.algostack.nir.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor (@ApplicationContext context : Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)


    fun saveToken(
        token: String, userName: String, userImage: String, userEmail: String, userId: String,
        number: Any,location: String
    ){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_NAME, userName)
        editor.putString(USER_IMAGE, userImage)
        editor.putString(USER_EMAIL, userEmail)
        editor.putString(USER_ID, userId)
        editor.putString(USER_NUMER, number.toString())
        editor.putString(Constants.USER_LOCATION, location)

        editor.apply()
    }

// update user name , phone , and image
    fun updateToken(
        userName: String, number: String,userImage: String
){
        val editor = prefs.edit()
        editor.putString(USER_NAME, userName)
        editor.putString(USER_NUMER, number)
        editor.putString(USER_IMAGE, userImage)
        editor.apply()
    }

    fun  getUserId() : String? {
        return prefs.getString(USER_ID, null)
    }
    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun getUserImage(): String? {
        return prefs.getString(USER_IMAGE, null)
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun getUserNumber(): String? {
        return prefs.getString(USER_NUMER, null)
    }

    fun getUserLocation(): String? {
        return prefs.getString(Constants.USER_LOCATION, null)
    }



    fun clearToken(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }



}