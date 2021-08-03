package com.example.gym

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val session = preferences.getBoolean("session" , false)


        if(session){
            goToNavigationActivity()
        }

        bt_login.setOnClickListener {
            createSessionPreferences()
            goToNavigationActivity()
        }
    }

    private fun createSessionPreferences() {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session" , true)
        editor.apply()
    }

    private fun goToNavigationActivity(){
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }
}