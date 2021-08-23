package com.example.gym.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gym.fragments.*

class ViewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle):  FragmentStateAdapter(fragment, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0->  ChartFragment()
            1->  AddStatFragment()
            2->  GoalFragment()
            3->  ListStatFragment()
            else ->   Fragment()
        }
    }
}