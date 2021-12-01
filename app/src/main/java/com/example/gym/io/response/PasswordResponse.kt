package com.example.gym.io.response

import com.google.gson.annotations.SerializedName

data class PasswordResponse (val success: Boolean/*,
                             @SerializedName("current_password") val currentPassword: Array<String>,
                             @SerializedName("new_password") val newPassword: Array<String>,
                             @SerializedName("confirm_password") val confirmPassword: Array<String>*/)




