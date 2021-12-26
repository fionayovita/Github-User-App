package com.example.githubuserapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubuserapp.ui.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = null

    override fun createFragment(position: Int): Fragment {
        return FollowingFragment.newInstance(position, username.toString())
    }

    override fun getItemCount(): Int {
        return 2
    }
}