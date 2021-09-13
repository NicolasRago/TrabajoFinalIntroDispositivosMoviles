package com.example.myapplication.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AnimalInfo(
    var id: Long?,
    var electronicId: Long,
    @SerializedName("fechaNacimiento")
    var dateBirth: Date,
    @SerializedName("ultimaFechaParto")
    var lastBirthDate: Date?,
    @SerializedName("cantidadPartos")
    var birthCount: Long,
    @SerializedName("peso")
    var weight: Float,
    var herdId: Long,
    var cowBcsId: Long,
    var fechaBcs: Date?,
    var cc: Float
)