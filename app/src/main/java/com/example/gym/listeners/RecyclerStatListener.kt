package com.example.gym.listeners

import com.example.gym.models.Body

interface RecyclerStatListener {
    fun onClickRemove(body: Body, position: Int)
}

