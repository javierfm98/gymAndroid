package com.example.gym.models

import com.google.gson.annotations.SerializedName

data class Training(val id: Int,
                    @SerializedName("user_id") val userId: Int,
                    val capacity: Int,
                    val enroll: Int,
                    val description: String,
                    @SerializedName("training_time") val trainingTime: String,
                    @SerializedName("training_day") val trainingDay: String)
