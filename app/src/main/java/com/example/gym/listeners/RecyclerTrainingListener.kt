package com.example.gym.listeners

import com.example.gym.models.Training

interface RecyclerTrainingListener {
    fun onClick(training: Training , position: Int)
    fun onReservation(training: Training , position: Int)
    fun onRemoveReservation(training: Training , position: Int)
    fun onNoMoreReservation(training: Training, position: Int)
}

