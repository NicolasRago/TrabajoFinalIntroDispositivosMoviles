package com.example.myapplication.provider.services

import com.example.myapplication.model.AnimalInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT

interface AnimalService {

    @POST("/api/cow")
    suspend fun createCow(@Body cow: AnimalInfo): Response<AnimalInfo>

    @GET("/api/cow/{id}")
    suspend fun getCow(@Path("id") id: Long): Response<AnimalInfo>
}