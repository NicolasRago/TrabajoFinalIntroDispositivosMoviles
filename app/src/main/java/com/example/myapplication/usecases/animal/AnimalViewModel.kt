package com.example.myapplication.usecases.animal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.AnimalAlert
import com.example.myapplication.model.AnimalInfo
import com.example.myapplication.provider.APIClient
import com.example.myapplication.provider.services.AlertService
import com.example.myapplication.provider.services.AnimalService
import kotlinx.coroutines.*

class AnimalViewModel : ViewModel() {
    // LiveData
    val animal = MutableLiveData<AnimalInfo>()
    val loading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val alertLoading = MutableLiveData<Boolean>()

    // Coroutine
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun registerAnimal(cow: AnimalInfo){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = APIClient.retrofit?.create(AnimalService::class.java)?.createCow(cow)
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    message.postValue("Animal creado con id: ${response.body()?.id}.")
                    loading.value = false
                } else {
                    onError("Error : ${response?.message()} ")
                }
            }
        }
    }

    fun getAnimal(id: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = APIClient.retrofit?.create(AnimalService::class.java)?.getCow(id.toLong())
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    animal.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response?.message()} ")
                }
            }
        }
    }

    fun registerAlert(animal: AnimalInfo,bcsThresholdsMax: String, bcsThresholdsMin: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = APIClient.retrofit?.create(AlertService::class.java)?.createAnimalAlarm(
                AnimalAlert(animal.id,bcsThresholdMax = bcsThresholdsMax.toFloat(), bcsThresholdMin = bcsThresholdsMin.toFloat())
            )
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    message.postValue("Alarma creada!")
                    alertLoading.value = false
                } else {
                    alertLoading.value = false
                    message.value = "Error : ${response?.message()} "
                }
            }
        }
    }

    private fun onError(message: String) {
        this.message.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}