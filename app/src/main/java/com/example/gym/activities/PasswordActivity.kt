package com.example.gym.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gym.R
import com.example.gym.toast
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*

class PasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        setSupportActionBar(toolbar)
        setTitle("Cambiar contraseña")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener {
            //toast("Click!!")
            finish()
        }


    }
}