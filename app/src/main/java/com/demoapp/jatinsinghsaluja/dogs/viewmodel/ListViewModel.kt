package com.demoapp.jatinsinghsaluja.dogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.model.DogDatabase
import com.demoapp.jatinsinghsaluja.dogs.model.DogsApiService
import com.demoapp.jatinsinghsaluja.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private val dogsApiService = DogsApiService()

    // allows to observe the observable of type Single returned by the remote API
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadingError = MutableLiveData<Boolean>()

    // this live data will tell its subscribers that the system is loading
    val loading = MutableLiveData<Boolean>()

    private var prefHelper = SharedPreferencesHelper(getApplication())

    fun refresh(){

        fetchFromRemote()

    }

    private fun fetchFromRemote(){

        loading.value = true
        disposable.add(
            dogsApiService.getDogs()
                    //access remote api on background thread
                .subscribeOn(Schedulers.newThread())
                    //process the response on main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<List<DogBreed>>(){
                    override fun onSuccess(dogList: List<DogBreed>) {

                        storeDogsLocally(dogList)

                    }

                    override fun onError(e: Throwable) {

                        dogsLoadingError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }

                })
        )

    }

    private fun dogsFetched(dogList:List<DogBreed>){

        dogs.value = dogList
        dogsLoadingError.value = false
        loading.value = false

    }

    private fun storeDogsLocally(dogList:List<DogBreed>){

        launch {
            val dogDao = DogDatabase(getApplication()).dogDao()
            dogDao.deleteAllDogs()
            val result = dogDao.insertAll(*dogList.toTypedArray())
            var i = 0
            while (i<dogList.size){
                dogList[i].uuid = result[i].toInt()
                ++i
            }
            dogsFetched(dogList)
        }
        prefHelper.saveUpdateTime(System.nanoTime())

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}