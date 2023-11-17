package com.algostack.nir.services.network

import com.algostack.nir.services.api.AuthIntercepter
import com.algostack.nir.services.api.PublicPostApi
import com.algostack.nir.services.api.UserApi
import com.algostack.nir.services.api.VerificationAPI
import com.algostack.nir.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)

    }

    @Singleton
    @Provides
    fun provideOkHtppClient(authIntercepter: AuthIntercepter) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authIntercepter).build()
    }



    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder: Retrofit.Builder) : UserApi {
        return retrofitBuilder.build().create(UserApi::class.java)
    }

    @Singleton
    @Provides
    fun providePublicPostApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : PublicPostApi {
        return retrofitBuilder
            .client(okHttpClient)
            .build().create(PublicPostApi::class.java)
    }





    @Singleton
    @Provides
    fun providesVerifyAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) : VerificationAPI {

        return retrofitBuilder
            .client(okHttpClient)
            .build().create(VerificationAPI::class.java)


    }
}