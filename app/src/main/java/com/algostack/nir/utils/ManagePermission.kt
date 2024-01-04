package com.algostack.nir.utils

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.algostack.nir.view.main.MainActivity


class ManagePermission (
    val context : Context,
    val list : List<String>,
    val code : Int
    ){

    // Check permission at runtime
    fun checkPermission() {
        if(isPermissionGranted() != PackageManager.PERMISSION_GRANTED){
          showAlertDialog()
        }else{
            Toast.makeText(context, "Permission already granted.", Toast.LENGTH_LONG).show()
        }
    }




    fun isPermissionGranted() : Int {
       // PERMISSION_GRANTED = 0
        // PERMISSION_DENIED = -1
        var counter = 0
        for(permission in list){
            counter += ContextCompat.checkSelfPermission(context, permission)
        }
        return counter

    }


    // Find the first denied permission
    private fun deniedPermission() : String {
        for(permission in list){
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED){
                return permission
            }
        }
        return ""
    }


    private fun showAlertDialog() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, permission)) {

            // Show an explanation asynchronously
            Toast.makeText(context, "Should show an explanation.", Toast.LENGTH_LONG).show()
        }else{
            ActivityCompat.requestPermissions(context as MainActivity, list.toTypedArray(), code)
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