package com.example.gym.models

import com.google.gson.annotations.SerializedName

data class Training(val id: Int,
                    @SerializedName("user_id") val userId: Int,
                    val day: String,
                    val capacity: Int,
                    val enroll: Int,
                    val description: String,
                    @SerializedName("time_format") val timeFormat: String)
