package com.algostack.nir.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkUtils {

    companion object{
        fun isInternetConnected(context: Context): Boolean{
            val connecttivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return when{
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ->{
                    val activityNetwork = connecttivityManager.activeNetwork ?: return false
                    val cap = connecttivityManager.getNetworkCapabilities(activityNetwork) ?: return false
                    when {
                        cap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        cap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }else -> {
                    // Use Deprecated method only on older devices
                    val activeNetwork = connecttivityManager.activeNetworkInfo ?: return false
                    return  when (activeNetwork.type){
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_VPN -> true
                        else -> false

                    }

                }
            }
        }
    }

}