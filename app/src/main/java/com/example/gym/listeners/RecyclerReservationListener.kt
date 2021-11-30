package com.example.gym.listeners

import com.example.gym.models.Training

interface RecyclerReservationListener {
    fun onClick(training: Training, position: Int)
}