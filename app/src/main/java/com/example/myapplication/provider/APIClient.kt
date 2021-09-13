package com.example.myapplication.provider

import com.example.myapplication.util.Constants
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import com.google.gson.GsonBuilder

import com.google.gson.Gson




object APIClient {

    var retrofit: Retrofit? = null

    init {
        retrofit = getClient();
    }

    private fun getClient(): Retrofit? {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()
        val client = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
        return retrofit
    }
}