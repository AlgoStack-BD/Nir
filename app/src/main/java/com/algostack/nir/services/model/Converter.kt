package com.algostack.nir.services.model

import androidx.room.TypeConverter
import com.google.gson.Gson


class Converter {

    @TypeConverter
    fun billsToString(bills: Bills): String {
        return Gson().toJson(bills)
    }

    @TypeConverter
    fun stringToBills(json: String): Bills {
        return Gson().fromJson(json, Bills::class.java)
    }

}