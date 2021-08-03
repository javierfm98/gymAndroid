package com.example.gym.models

import java.sql.Time
import java.util.*

data class Training(val id: Int, val user_id: Int, val date: String, val start: String, val end: String, val capacity: Int, val enroll: Int, val description: String)
