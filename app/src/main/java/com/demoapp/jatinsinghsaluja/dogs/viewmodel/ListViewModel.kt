package com.demoapp.jatinsinghsaluja.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed

class ListViewModel: ViewModel() {

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadingError = MutableLiveData<Boolean>()

    // this live data will tell its subscribers that the system is loading
    val loading = MutableLiveData<Boolean>()

    fun refresh(){

        val dog1 = DogBreed("1","Corgi", "15 years",
            "breed group", "bred for", "temperament", "")
        val dog2 = DogBreed("2","Labrador", "18 years",
            "breed group", "bred for", "temperament", "")
        val dog3 = DogBreed("3","Rotwailer", "20 years",
            "breed group", "bred for", "temperament", "")

        val dogsList = arrayListOf<DogBreed>(dog1, dog2, dog3)

        dogs.value =dogsList
        dogsLoadingError.value = false
        loading.value = false
    }
}