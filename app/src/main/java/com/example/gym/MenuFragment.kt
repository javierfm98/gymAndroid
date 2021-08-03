package com.example.gym


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import devs.mulham.horizontalcalendar.HorizontalCalendar
import kotlinx.android.synthetic.main.fragment_menu.view.*
import java.util.*


class MenuFragment : Fragment()  {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("")

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_menu, container, false)

        rootView.card_training.setOnClickListener {
            loadFragment(TrainingsFragment())
        }

        rootView.cardViewLogout.setOnClickListener {
            clearSessionPreferences(rootView.context)
            val intent = Intent(rootView.context, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }



        return rootView
    }

    private fun clearSessionPreferences(context: Context) {
        val preferences = context.getSharedPreferences("general" , Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("session" , false)
        editor.apply()
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = (activity as FragmentActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

}