package com.demoapp.jatinsinghsaluja.dogs.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {

    private val dogsApiService = DogsApiService()

    // allows to observe the observable of type Single returned by the remote API
    private val disposable = CompositeDisposable()

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadingError = MutableLiveData<Boolean>()

    // this live data will tell its subscribers that the system is loading
    val loading = MutableLiveData<Boolean>()

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
                    override fun onSuccess(t: List<DogBreed>) {

                        dogs.value = t
                        dogsLoadingError.value = false
                        loading.value = false

                    }

                    override fun onError(e: Throwable) {

                        dogsLoadingError.value = true
                        loading.value = false
                        e.printStackTrace()

                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}