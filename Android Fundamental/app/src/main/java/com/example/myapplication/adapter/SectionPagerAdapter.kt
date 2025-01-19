@file:Suppress("DEPRECATION")

package com.example.myapplication.adapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.myapplication.R
import com.example.myapplication.fragment.FragmentFollower
import com.example.myapplication.fragment.FragmentFollowing

class SectionPagerAdapter (
    private val context : Context,
    fm : FragmentManager,
    bundle : Bundle
        ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentBundle = Bundle()
    @StringRes
    private val tabTitle = intArrayOf(R.string.tab_1, R.string.tab_2)

    init {
        fragmentBundle = bundle
    }

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position){
            0 -> fragment = FragmentFollower()
            1 -> fragment = FragmentFollowing()
        }
        fragment?.arguments = this.fragmentBundle
        return fragment as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(tabTitle[position])
    }
}