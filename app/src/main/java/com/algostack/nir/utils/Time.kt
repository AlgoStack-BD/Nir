package com.algostack.nir.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.inject.Inject

class Time @Inject constructor (@ApplicationContext context : Context) {

    fun timeStamp(): String {

        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val time = sdf.format(Date(timeStamp.time))

        return time.toString()
    }
}