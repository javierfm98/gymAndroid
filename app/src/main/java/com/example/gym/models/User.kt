package com.example.gym.models

import com.google.gson.annotations.SerializedName

data class User (
    val id: Int,
    val name: String,
    val surname: String,
    val phone: String,
    val username: String,
    val email: String,
    @SerializedName("role_id") val roleId : Int,
    val photo : Photo)