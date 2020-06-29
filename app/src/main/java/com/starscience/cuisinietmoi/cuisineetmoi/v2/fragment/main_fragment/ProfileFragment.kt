package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.ItemUserInfoBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.profile.LastPublished
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.welcome_fragment.LoginActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.UserInfoViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.UserViewPagerAdapter

class ProfileFragment : Fragment(), Toolbar.OnMenuItemClickListener{
    lateinit var bindingUserInfo: ItemUserInfoBinding
    lateinit var userInfoViewModel: UserInfoViewModel

    lateinit var toolbar: Toolbar
    lateinit var progressBar: RelativeLayout
    lateinit var user_base_container: ConstraintLayout
    lateinit var tabLayout: TabLayout
    lateinit var pagerAdapter: UserViewPagerAdapter
    lateinit var viewPager: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        progressBar = view.findViewById(R.id.user_infos_progress_bar)
        toolbar = view.findViewById(R.id.top_toolbar)
        toolbar.setOnMenuItemClickListener(this)
        tabLayout = view.findViewById(R.id.user_tab)
        viewPager = view.findViewById(R.id.user_view_pager)


        val fragmentList = listOf<Fragment>(LastPublished(), Fragment(), Fragment())
        val fragmentTitleList = listOf<String>("Last Published", "Most Popular", "All")

        pagerAdapter = UserViewPagerAdapter(childFragmentManager, fragmentList, fragmentTitleList)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        user_base_container = view.findViewById(R.id.current_user_base_container)
        bindingUserInfo = DataBindingUtil.inflate(inflater, R.layout.item_user_info, container, false)
        user_base_container.addView(bindingUserInfo.root)
        userInfoViewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(UserInfoViewModel::class.java)
        userInfoViewModel.loadingVisibility.observe(this, Observer { value -> if(value == true) showLoading() else hideLoading() })
        bindingUserInfo.viewModel = userInfoViewModel

        return view
    }

    private fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    // Top toolbar item
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
                return true
            }
        }
        return false
    }
}