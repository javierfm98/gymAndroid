package com.example.gym


import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.gym.io.ApiService
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

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
        textInputEmail.setText(email)

        Picasso.get()
            .load(urlPhoto)
            .fit()
            .transform(CircleTransform())
            .into(imageViewProfileEdit)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu , menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.itemSave -> {
              /*  apellido.setText("Fernández")
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(apellido, InputMethodManager.SHOW_IMPLICIT)
                apellido.hideKeyboard()*/
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}