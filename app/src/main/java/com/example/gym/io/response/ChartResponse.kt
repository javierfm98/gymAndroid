package com.example.gym.io.response

data class ChartResponse(
    val countMonths: ArrayList<String>,
    val goalWeightCount: ArrayList<Float>,
    val goalBodyFatCount: ArrayList<Float>,
    val arrayWeight: ArrayList<Float>,
    val pointStartWeight: Int,
    val arrayBodyFat: ArrayList<Float>,
    val pointStartBodyFat: Int
)