package com.example.myapplication.provider.services

import com.example.myapplication.model.HerdInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HerdService {

    @GET("/api/herd/{id}")
    suspend fun getCow(@Path("id") id: Long): Response<HerdInfo>
}