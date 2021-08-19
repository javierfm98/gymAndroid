package com.example.gym.models

import com.google.gson.annotations.SerializedName

data class Subscription (val id: Int,
                         @SerializedName("user_id") val userId : Int,
                         @SerializedName("rate_id") val rateId: Int,
                         val status: Int,
                         @SerializedName("create_at") val createAt: String,
                         @SerializedName("subs_end") val subsEnd: String)

