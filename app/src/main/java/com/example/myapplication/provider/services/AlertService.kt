package com.example.myapplication.provider.services

import com.example.myapplication.model.AnimalAlert
import com.example.myapplication.model.HerdAlert
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AlertService {

    @POST("/api/herdAlert")
    suspend fun createHerdAlarm(@Body herdAlert: HerdAlert): Response<HerdAlert>

    @POST("/api/cowAlert")
    suspend fun createAnimalAlarm(@Body animalAlert: AnimalAlert): Response<AnimalAlert>
}