package com.algostack.nir.utils

import android.content.Context
import com.algostack.nir.utils.Constants.PREFS_TOKEN_FILE
import com.algostack.nir.utils.Constants.USER_EMAIL
import com.algostack.nir.utils.Constants.USER_IMAGE
import com.algostack.nir.utils.Constants.USER_NAME
import com.algostack.nir.utils.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor (@ApplicationContext context : Context) {

    private var prefs = context.getSharedPreferences(PREFS_TOKEN_FILE, Context.MODE_PRIVATE)


    fun saveToken(token : String, userName : String, userImage : String, userEmail : String){
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_NAME, userName)
        editor.putString(USER_IMAGE, userImage)
        editor.putString(USER_EMAIL, userEmail)
        editor.apply()
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

    fun clearToken(){
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }



}