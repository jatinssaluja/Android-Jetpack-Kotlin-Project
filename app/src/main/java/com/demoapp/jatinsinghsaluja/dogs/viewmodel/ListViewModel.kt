package com.demoapp.jatinsinghsaluja.dogs.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.demoapp.jatinsinghsaluja.dogs.model.DogBreed
import com.demoapp.jatinsinghsaluja.dogs.model.DogDatabase
import com.demoapp.jatinsinghsaluja.dogs.model.DogsApiService
import com.demoapp.jatinsinghsaluja.dogs.util.NotificationsHelper
import com.demoapp.jatinsinghsaluja.dogs.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*

class ListViewModel(application: Application): BaseViewModel(application) {

    private val dogsApiService = DogsApiService.getDogsApiService()

    var job: Job? = null
    val coroutineExceptionHandler = CoroutineExceptionHandler{
            coroutineContext, throwable -> onError("Exception: ${throwable.localizedMessage}") }

    private var prefHelper = SharedPreferencesHelper(getApplication())

    // 5 minutes in nano seconds
    private var refreshTime = 5 * 60 * 1000 * 1000 * 1000L

    val dogs = MutableLiveData<List<DogBreed>>()
    val dogsLoadingError = MutableLiveData<Boolean>()

    // this live data will tell its subscribers that the system is loading
    val loading = MutableLiveData<Boolean>()


    fun refresh(){

        checkCacheDuration()

        val updateTime = prefHelper.getUpdateTime()
        if(updateTime != null && updateTime!= 0L && System.nanoTime() - updateTime < refreshTime){
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }

    }

    private fun checkCacheDuration(){

        val cachePreference = prefHelper.getCacheDuration()
        try{
            val cachePreferenceInt = cachePreference?.toInt() ?: 5*60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        }catch (ex:NumberFormatException){
            ex.printStackTrace()
        }
    }

    fun refreshBypassCache(){
        fetchFromRemote()
    }

    private fun fetchFromDatabase(){

        loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsFetched(dogs)
            Toast.makeText(getApplication(), "Dogs Fetched from database",Toast.LENGTH_LONG).show()
        }

    }

    private fun fetchFromRemote(){

        loading.value = true

        job = CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val response = dogsApiService.getDogs()

            withContext(Dispatchers.Main){

                if(response.isSuccessful){

                    response.body()?.let{ storeDogsLocally(it)}
                    Toast.makeText(getApplication(), "Dogs Fetched from remote api",Toast.LENGTH_LONG).show()
                    NotificationsHelper(getApplication()).createNotification()

                }else{
                    onError("Error: ${response.message()}")
                }
            }
        }


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

    private fun onError(message: String) {

        dogsLoadingError.value = true
        loading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}