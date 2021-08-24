package com.example.gym.io.response

import com.google.gson.annotations.SerializedName

data class GoalResponse (@SerializedName("number_goals_weight") val goalsWeight : Float,
                         @SerializedName("number_goals_body_fat") val goalsBodyFat : Float)