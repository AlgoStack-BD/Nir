package com.algostack.nir.services.network

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Room
import com.algostack.nir.services.db.NirLocalDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
//        context,NirLocalDB::class.java, "NirLocalDB"
//    ).build()

    @Volatile
    private var INSTANCE: NirLocalDB ?= null

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : NirLocalDB{
        if (INSTANCE == null){
            synchronized(this){
                INSTANCE = Room.databaseBuilder(
                    context,NirLocalDB::class.java, "NirLocalDB"
                ).build()
            }
        }

        return INSTANCE!!
    }

}