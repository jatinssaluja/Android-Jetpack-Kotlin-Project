package com.demoapp.jatinsinghsaluja.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed

class DetailViewModel: ViewModel() {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(){

        val dog = DogBreed("1","Corgi", "15 years",
            "breed group", "bred for", "temperament", "")

        dogLiveData.value = dog
    }
}