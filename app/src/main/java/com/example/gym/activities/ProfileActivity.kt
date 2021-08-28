package com.example.gym.activities


import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.models.User
import com.example.gym.toast
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream


class ProfileActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    private var stream: InputStream? = null
    private  var name: String = ""


    val GALLERY_PHOTO = 1
    val permissionReadStorage = android.Manifest.permission.READ_EXTERNAL_STORAGE
    val getImage = registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->

        if(uri != null){
            Picasso.get()
                .load(uri)
                .fit()
                .transform(CircleTransform())
                .into(imageViewProfileEdit)

            val cursor: Cursor? = this.getContentResolver().query(uri, null, null, null, null)

            cursor?.use{
                it.moveToFirst()
                name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                cursor.close()
            }

             stream = contentResolver.openInputStream(uri)


         //  uploadImage(stream!!,name)


        }


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
                uploadImage(stream!!,name)
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


    private fun uploadImage(file: InputStream, name: String) {
        val preferences = getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
      //  val file = File(image.toString())

        val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file.readBytes())

        val fileToUpload = MultipartBody.Part.createFormData("image", "myPic", requestBody)


        val call = apiService.uploadImage("Bearer $jwt", fileToUpload, name)
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                //    toast("Bien")
                }else{
                 //   toast("Error!!")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })

    }




    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (null != activity.currentFocus) imm.hideSoftInputFromWindow(
            activity.currentFocus!!
                .applicationWindowToken, 0
        )
    }



}




