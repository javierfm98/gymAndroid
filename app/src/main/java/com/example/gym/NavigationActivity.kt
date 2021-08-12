package com.example.gym


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.gym.fragments.*
import com.example.gym.io.ApiService
import com.example.gym.utils.CircleTransform
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NavigationActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var toggle: ActionBarDrawerToggle

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setNavDrawer()

        val preferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE)

        val header: View = nav_view.getHeaderView(0)
        val fullName = preferences.getString("name" , "") + " " + preferences.getString("surname" , "")
        val urlPhoto = "http://64.225.72.59/img/"+preferences.getString("photo" , "")
        header.textViewHeaderName.text = fullName


        Picasso.get()
            .load(urlPhoto)
            .fit()
            .transform(CircleTransform())
            .into(header.imageViewProfile)




        if(savedInstanceState == null){
            loadFragment(MenuFragment())
            nav_view.menu.getItem(0).isChecked = true
        }


    }

    private fun setNavDrawer() {
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.isDrawerIndicatorEnabled = true
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
    }

    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.content , fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_menu -> loadFragment(MenuFragment())
            R.id.nav_calendar-> loadFragment(TrainingsFragment())
            R.id.nav_reservation -> loadFragment(ReservationFragment())
            R.id.nav_subscription -> loadFragment(SubscriptionFragment())
            R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
            }
            R.id.nav_logout -> performLogout()
        }

        drawer_layout.closeDrawers()
        return true
    }

    private fun performLogout() {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreferences()
                clearUserPreferences()
                val intent = Intent(this@NavigationActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun clearSessionPreferences() {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("session" , "")
        editor.apply()
    }

    private fun clearUserPreferences() {
        val preferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("id" , -1)
        editor.putString("name" , "")
        editor.putString("surname" , "")
        editor.putString("phone", "")
        editor.putString("username" , "")
        editor.putString("email" , "")
        editor.putString("photo" , "")
        editor.apply()
    }

/*
    //Configuration toggle (icono hamburguesa)
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawers()
        }else{
            super.onBackPressed()
        }

    }


}