package com.example.myapplication.model

data class HerdAlert(
    var herdId: Long?,
    var bcsThresholdMax: Float,
    var bcsThresholdMin: Float
)
