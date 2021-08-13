package com.example.gym.io.response

import com.example.gym.models.Training


data class CheckResponse (val success: Boolean,
                          val training: Training)