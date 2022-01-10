package com.example.gym.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.io.response.LoginResponse
import com.example.gym.models.User
import com.example.gym.toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val session = preferences.getString("session" , "")


        if (session != null) {
            if(session.contains(".")){
                goToNavigationActivity()
            }
        }

        bt_login.setOnClickListener {
            hideKeyboard(this)
            //validates
            performLogin()
        }
    }

    private fun performLogin() {

        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        if(email.trim().isEmpty() || password.trim().isEmpty()){
            toast("Por favor ingrese correo y contraseña")
            return
        }

        val call = apiService.postLogin(email , password)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    if(loginResponse == null){
                        toast("Login nulo")
                        return
                    }
                    if(loginResponse.success){
                        createSessionPreferences(loginResponse.token)
                        createUserPreferences(loginResponse.user)
                        goToNavigationActivity()
                    }else{
                        toast("Las credenciales no son correctas")
                    }
                }else{
                    toast("Se obtuvo una respuesta inesperada del servidor")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun createSessionPreferences(token: String) {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("session" , token)
        editor.apply()
    }

    private fun createUserPreferences(user: User){
        val preferences = getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("id" , user.id)
        editor.putString("name" , user.name)
        editor.putString("surname" , user.surname)
        editor.putString("phone" , user.phone)
        editor.putString("username" , user.username)
        editor.putString("email" , user.email)
        editor.putString("photo" , user.photo.route)
        editor.putInt("paymentStatus" , user.paymentStatus)
        editor.apply()
    }

    private fun goToNavigationActivity(){
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }

}