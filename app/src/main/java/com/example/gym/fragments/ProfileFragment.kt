package com.example.gym.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gym.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation.*


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        activity?.setTitle("")

        val navigationView = activity?.nav_view as NavigationView
        navigationView.setCheckedItem(R.id.nav_profile)

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_profile, container, false)


        return rootView
    }

}