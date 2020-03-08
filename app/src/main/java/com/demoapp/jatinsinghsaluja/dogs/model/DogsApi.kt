package com.demoapp.jatinsinghsaluja.dogs.model

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface DogsApi {

    @GET("DevTides/DogsApi/master/dogs.json")
    suspend fun getDogs(): Response<List<DogBreed>>
}