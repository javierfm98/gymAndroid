package com.example.gym.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.gym.MainActivity
import com.example.gym.ProfileActivity
import com.example.gym.R
import com.example.gym.io.ApiService
import com.example.gym.toast
import kotlinx.android.synthetic.main.fragment_menu.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment()  {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("")

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        rootView.card_training.setOnClickListener {
            loadFragment(TrainingsFragment())
        }

        rootView.cardViewReservation.setOnClickListener {
            loadFragment(ReservationFragment())
        }

        rootView.cardViewSubscription.setOnClickListener {
            loadFragment(SubscriptionFragment())
        }

        rootView.cardViewProfile.setOnClickListener {
            //loadFragment(ProfileFragment())
            activity?.let{
                val intent = Intent (it, ProfileActivity::class.java)
                it.startActivity(intent)
            }
        }

        rootView.cardViewLogout.setOnClickListener {
            performLogout(rootView.context)
        }





        return rootView
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

    private fun loadFragment(fragment: Fragment){
        val transaction = (activity as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

}