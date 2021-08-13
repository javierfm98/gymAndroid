package com.example.gym.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.gym.*
import com.example.gym.io.ApiService
import com.example.gym.models.User
import com.example.gym.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.android.synthetic.main.fragment_menu.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
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

        updateDataProfile(rootView.context)

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

        changeNavHeader()
    }

    private fun setDataProfile(context: Context) {
        val preferences = context.getSharedPreferences("userInfo" , Context.MODE_PRIVATE)

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

    private  fun changeNavHeader(){
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

    private fun loadFragment(fragment: Fragment){
        val transaction = (activity as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }



}