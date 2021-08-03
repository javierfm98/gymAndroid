package com.example.gym

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Activity.toast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this,message,duration).show()

fun Activity.toast(resourceId: Int, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(this,resourceId,duration).show()

fun ViewGroup.inflate(layoutId: Int) = LayoutInflater.from(context).inflate(layoutId,this,false)!!

