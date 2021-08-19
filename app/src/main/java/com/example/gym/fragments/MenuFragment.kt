package com.example.gym.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.gym.*
import com.example.gym.activities.MainActivity
import com.example.gym.activities.NavigationActivity
import com.example.gym.activities.ProfileActivity
import com.example.gym.io.ApiService
import com.example.gym.models.User
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment()  {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }

    private lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_menu, container, false)

       // updateDataProfile(rootView.context)

        return rootView
    }

    override fun onResume() {
        super.onResume()
       // activity?.toast("Inicio menu")
        updateDataProfile(rootView.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = Navigation.findNavController(view)

        card_training.setOnClickListener {
            navController.navigate(R.id.trainingsFragment)
        }

        cardViewReservation.setOnClickListener {
            navController.navigate(R.id.reservationFragment)
        }

        cardViewSubscription.setOnClickListener {

        }

        cardViewProfile.setOnClickListener {
            activity?.let{
                val intent = Intent (it, ProfileActivity::class.java)
                it.startActivity(intent)
            }
        }

        cardViewLogout.setOnClickListener {
            performLogout(view.context)
        }




        super.onViewCreated(view, savedInstanceState)
    }

    private fun updateDataProfile(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.getUser("Bearer $jwt")
        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful){
                    val user = response.body()
                    user?.let {
                        createUserPreferences(user,context)
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })

    }

    private fun createUserPreferences(user: User, context: Context){
        val preferences = context.getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt("id" , user.id)
        editor.putString("name" , user.name)
        editor.putString("surname" , user.surname)
        editor.putString("phone" , user.phone)
        editor.putString("username" , user.username)
        editor.putString("email" , user.email)
        editor.putString("photo" , user.photo.route)
        editor.apply()

        (activity as NavigationActivity).changeNavHeaderData()
    }


    private fun performLogout(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val jwt = preferences.getString("session" , "")
        val call = apiService.postLogout("Bearer $jwt")
        call.enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreferences(context)
                clearUserPreferences(context)
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                activity?.toast(t.localizedMessage)
            }

        })
    }

    private fun clearSessionPreferences(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("session" , "")
        editor.apply()
    }

    private fun clearUserPreferences(context: Context) {
        val preferences = context.getSharedPreferences("userInfo" , Context.MODE_PRIVATE)
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




}