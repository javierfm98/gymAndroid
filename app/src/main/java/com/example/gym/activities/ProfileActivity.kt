package com.example.gym.activities


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gym.R
import com.example.gym.databinding.ActivityMainBinding
import com.example.gym.io.ApiService
import com.example.gym.models.User
import com.example.gym.toast
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    val GALLERY_PHOTO = 1
    val permissionReadStorage = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val getImage = registerForActivityResult(ActivityResultContracts.GetContent(),
        ActivityResultCallback {

            Picasso.get()
                .load(it)
                .fit()
                .transform(CircleTransform())
                .into(imageViewProfileEdit)

            val path = it.path
            toast("$path")
        })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setSupportActionBar(toolbar)
        setTitle("Perfil")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)



        getDataProfile()

        toolbar.setNavigationOnClickListener {
            //toast("Click!!")
            finish()
        }

        imageViewProfileEdit.setOnClickListener {
            //toast("Click Foto")
            selectPhotoGallery()

        }
    }

    private fun selectPhotoGallery() {
        requestPermissionsGallery()
    }

    private fun requestPermissionsGallery() {
        requestPermissions(arrayOf(permissionReadStorage), GALLERY_PHOTO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            GALLERY_PHOTO ->{
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getImage.launch("image/*")
                }else{
                    toast("No diste el permiso para acceder a la galería")
                }
            }
        }
    }



    private fun getDataProfile() {
        val preferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
        val name = preferences.getString("name" , "")
        val surname = preferences.getString("surname" , "")
        val phone = preferences.getString("phone" , "")
        val username = preferences.getString("username" , "")
        val email = preferences.getString("email" , "")
        val urlPhoto = "http://64.225.72.59/img/"+preferences.getString("photo" , "")

        textInputName.setText(name)
        textInputSurname.setText(surname)
        textInputUsername.setText(username)
        textInputPhone.setText(phone)
        textInputEmail.setText(email)

        Picasso.get()
            .load(urlPhoto)
            .fit()
            .transform(CircleTransform())
            .into(imageViewProfileEdit)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.itemSave -> {
                hideKeyboard(this)
                saveProfile()
                window.decorView.clearFocus()
            }

        }
        return super.onOptionsItemSelected(item)
    }



    private fun saveProfile() {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")

        val name = textInputName.text.toString()
        val surname = textInputSurname.text.toString()
        val phone = textInputPhone.text.toString()
        val username = textInputUsername.text.toString()
        val email = textInputEmail.text.toString()

        val call = apiService.updateProfile("Bearer $jwt", name, surname, phone, username, email)
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val user = response.body()
                    toast("Perfil guardado")
                    user?.let {
                        updateDataProfile(user)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun updateDataProfile(user: User) {
        val preferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("id" , user.id)
        editor.putString("name" , user.name)
        editor.putString("surname" , user.surname)
        editor.putString("phone" , user.phone)
        editor.putString("username" , user.username)
        editor.putString("email" , user.email)
       // editor.putString("photo" , user.photo.route)
        editor.apply()
    }


    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }

}


