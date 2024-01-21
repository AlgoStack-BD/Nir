package com.algostack.nir.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat



class ManagePermission (
    val activity: Activity,
    val list : List<String>,
    val code : Int
    ){

    // Check permission at runtime
    fun checkPermission() {
        if(isPermissionGranted() != PackageManager.PERMISSION_GRANTED){
          showAlertDialog()
        }else{
            Toast.makeText(activity, "Permission already granted.", Toast.LENGTH_LONG).show()
        }
    }




    fun isPermissionGranted() : Int {
       // PERMISSION_GRANTED = 0
        // PERMISSION_DENIED = -1
        var counter = 0
        for(permission in list){
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter

    }


    // Find the first denied permission
    private fun deniedPermission() : String {
        for(permission in list){
            if(ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED){
                return permission
            }
        }
        return ""
    }


    private fun showAlertDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
        builder.setTitle("Need Permission")
        builder.setMessage("This app needs permission to use this feature.")
        builder.setPositiveButton("OK") { dialog, which ->
            requestPermission()
        }
        builder.setNegativeButton("Cancel", null)
        val dialog = builder.create()
        builder.show()
    }

    // Requesting permission
    private fun requestPermission() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity , permission)) {

            // Show an explanation asynchronously
            Toast.makeText(activity, "Should show an explanation.", Toast.LENGTH_LONG).show()
        }else{
            ActivityCompat.requestPermissions(activity , list.toTypedArray(), code)
        }
    }

    // Process permission result
    fun processResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
      var result = 0
        if(grantResults.isNotEmpty()){
            for(grant in grantResults){
                result += grant
            }
        }
        if(result == PackageManager.PERMISSION_GRANTED){
            return true
        }

        return false
    }


}