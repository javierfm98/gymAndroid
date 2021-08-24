package com.example.gym.models

import com.google.gson.annotations.SerializedName

data class Body(
    val id: Int,
    @SerializedName("user_id") val UserId: Int,
    @SerializedName("stat_id") val statId: Int,
    val value: Float,
    @SerializedName("date_format") val dateFormat: String,
    @SerializedName("date_beauty_format") val dateBeautyFormat: String
)