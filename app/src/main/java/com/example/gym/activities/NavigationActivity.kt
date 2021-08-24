package com.example.gym.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gym.R
import com.example.gym.fragments.*
import com.example.gym.io.ApiService
import com.example.gym.toast
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NavigationActivity : AppCompatActivity()  {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var listener: NavController.OnDestinationChangedListener

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        setNavDrawer()

    }


    private fun setNavDrawer() {
        setSupportActionBar(toolbar)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph , drawer_layout)

        nav_view.setupWithNavController(navController)
        setupActionBarWithNavController(navController,appBarConfiguration)

        val logoutMenuItem = nav_view.menu.findItem(R.id.nav_logout)

        logoutMenuItem.setOnMenuItemClickListener {
            performLogout()
            true
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

   /* override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> performLogout()
        }

        drawer_layout.closeDrawers()
        return true
    }*/


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

     fun changeNavHeaderData(){
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
    }

    override fun onBackPressed() {
        if(drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawers()
        }else{
            super.onBackPressed()
        }

    }


}