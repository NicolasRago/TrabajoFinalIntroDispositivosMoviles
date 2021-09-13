package com.example.myapplication.usecases.herd

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.HerdAlert
import com.example.myapplication.model.HerdInfo
import com.example.myapplication.provider.APIClient
import com.example.myapplication.provider.services.AlertService
import com.example.myapplication.provider.services.HerdService
import kotlinx.coroutines.*

class HerdViewModel : ViewModel() {

    // LiveData
    val herd = MutableLiveData<HerdInfo>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val alertLoading = MutableLiveData<Boolean>()

    // Coroutine
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getRodeo(id: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = APIClient.retrofit?.create(HerdService::class.java)?.getCow(id.toLong())
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    herd.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response?.message()} ")
                }
            }
        }
    }

    fun registerAlert(herd: HerdInfo, bcsThresholdsMax: String, bcsThresholdsMin: String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = APIClient.retrofit?.create(AlertService::class.java)?.createHerdAlarm(
                HerdAlert(herd.id,bcsThresholdMax = bcsThresholdsMax.toFloat(), bcsThresholdMin = bcsThresholdsMin.toFloat())
            )
            withContext(Dispatchers.Main) {
                if (response?.isSuccessful == true) {
                    errorMessage.postValue("Alarma creada!")
                    alertLoading.value = false
                } else {
                    alertLoading.value = false
                    errorMessage.value = "Error : ${response?.message()} "
                }
            }
        }
    }

    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}