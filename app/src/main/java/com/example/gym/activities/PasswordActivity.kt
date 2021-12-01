package com.example.gym.activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.io.response.PasswordResponse
import com.example.gym.toast
import kotlinx.android.synthetic.main.activity_password.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }


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

        buttonSavePassword.setOnClickListener {
            changePassword()
        }


    }

    private fun changePassword() {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")

        val currentPassword = textInputCurrentPassword.text.toString()
        val newPassword = textInputNewPassword.text.toString()
        val confirmPassword = textInputConfirmPassword.text.toString()

        val call = apiService.updatePassword("Bearer $jwt", currentPassword, newPassword, confirmPassword)
        call.enqueue(object: Callback<PasswordResponse> {
            override fun onResponse(call: Call<PasswordResponse>, response: Response<PasswordResponse>) {
                if(response.isSuccessful){
                    val response = response.body()
                    if (response != null) {
                        if(response.success){
                            toast("Contraseña actualizada")
                            clearTextInput()
                        }else{
                            toast("Error al cambiar la contraseña")
                            clearTextInput()
                        }
                    }

                }
            }

            override fun onFailure(call: Call<PasswordResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }

    private fun clearTextInput() {
        textInputCurrentPassword.setText("")
        textInputNewPassword.setText("")
        textInputConfirmPassword.setText("")
        hideKeyboard(this)
        window.decorView.clearFocus()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }

}
