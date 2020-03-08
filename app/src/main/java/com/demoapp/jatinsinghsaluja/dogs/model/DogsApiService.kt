package com.demoapp.jatinsinghsaluja.dogs.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DogsApiService {

    private val BASE_URL = "https://raw.githubusercontent.com"

    fun getDogsApiService():DogsApi{

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogsApi::class.java)

    }

}