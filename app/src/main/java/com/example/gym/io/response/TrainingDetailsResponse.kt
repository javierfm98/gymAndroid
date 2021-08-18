package com.example.gym.io.response


import com.example.gym.models.Training
import com.example.gym.models.User

data class TrainingDetailsResponse (val allUsers: ArrayList<User>,
                                    val training: Training)