package com.example.gym.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.gym.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val session = preferences.getString("session" , "")


        if (session != null) {
            if(session.contains(".")){
                goToNavigationActivity()
            }else{
                goToMainActivity()
            }
        }else{
            goToMainActivity()
        }


    }

    private fun goToMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 500)
    }

    private fun goToNavigationActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, NavigationActivity::class.java)
            startActivity(intent)
        }, 500)
    }
}