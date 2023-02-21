package com.udacity.asteroidradar.data.ws

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.presentation.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NasaRetrofit {

    var nasaApi: NasaWS

    init {
        val moshi = Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL) //Базовая часть адреса
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //Конвертер, необходимый для преобразования JSON'а в объекты
            .build()
        nasaApi = retrofit.create(NasaWS::class.java)
    }
}