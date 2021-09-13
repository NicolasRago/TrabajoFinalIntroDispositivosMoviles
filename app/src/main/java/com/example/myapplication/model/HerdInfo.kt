package com.example.myapplication.model

import com.google.gson.annotations.SerializedName

data class HerdInfo(
    var id: Long,
    var location: String,
    var bcsPromedio: Float,
    @SerializedName("cows")
    var animalsInfo: List<AnimalInfo>
)
