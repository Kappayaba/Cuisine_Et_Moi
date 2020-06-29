package com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class UserViewPagerAdapter(fragmentManager: FragmentManager, private val fragmentList: List<Fragment>, private val fragmentTitleList: List<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList[position]
    }
}