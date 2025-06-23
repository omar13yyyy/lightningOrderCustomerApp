package com.lightning.androidfrontend.data

import com.lightning.androidfrontend.data.api.VisitorApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.111:8000/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val visitorApi: VisitorApi by lazy {
        retrofit.create(VisitorApi::class.java)
    }
}

