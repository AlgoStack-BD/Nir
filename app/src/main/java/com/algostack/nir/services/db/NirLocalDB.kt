package com.algostack.nir.services.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.algostack.nir.services.model.Converter
import com.algostack.nir.services.model.PublicPostData
import com.algostack.nir.services.model.userResponseData

@Database(
    entities = [PublicPostData::class],
    version = 4
)
@TypeConverters(Converter::class)
abstract class NirLocalDB : RoomDatabase() {

    abstract fun getPublicPostDao(): PublicPostDao

}