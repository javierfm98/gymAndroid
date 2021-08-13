package com.example.gym.io.response

import com.example.gym.models.User

data class LoginResponse (val success: Boolean ,
                          val user: User ,
                          val token: String)
