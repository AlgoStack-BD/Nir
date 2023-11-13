package com.algostack.nir.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.TextView
import com.algostack.nir.R

object AlertDaialog {

    fun showCustomAlertDialogBox( context: Context ,msg : String){
        val view = LayoutInflater.from(context).inflate(R.layout.custom_alert_box, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val alert = builder.create()
        alert.setCancelable(true)

        val cancelBtn = view.findViewById<TextView>(R.id.cencelbtn)
        val okBtn = view.findViewById<TextView>(R.id.okBtn)
        val textView = view.findViewById<TextView>(R.id.alertText)

        textView.text = msg

        cancelBtn.setOnClickListener {
            alert.dismiss()
        }

        okBtn.setOnClickListener {
            alert.dismiss()
        }

        alert.window?.setBackgroundDrawable(ColorDrawable(0))
        alert.show()



    }

}