package com.algostack.nir.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.algostack.nir.R
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import de.hdodenhof.circleimageview.CircleImageView

object AlertDaialog {

    fun showCustomAlertDialogBox( context: Context ,msg : String){
        val view = LayoutInflater.from(context).inflate(R.layout.custom_alert_box, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)


        val okBtn = view.findViewById<LinearLayout>(R.id.noCustomokBtn)
        val textView = view.findViewById<TextView>(R.id.alertText)

        textView.text = msg



        okBtn.setOnClickListener {
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()



    }


    fun showCustomDoneDialogBox( context: Context ,msg : String){
        val view = LayoutInflater.from(context).inflate(R.layout.successfulldonedialog, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)


        val okBtn = view.findViewById<LinearLayout>(R.id.nodoneCustomokBtn)
        val textView = view.findViewById<TextView>(R.id.alertText)

        textView.text = msg



        okBtn.setOnClickListener {
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()



    }

    fun noInternetConnectionAlertBox( context: Context){
        val view = LayoutInflater.from(context).inflate(R.layout.nointernetconnection, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)

        val okBtn = view.findViewById<LinearLayout>(R.id.noInternetokBtn)



        okBtn.setOnClickListener {
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()



    }




}