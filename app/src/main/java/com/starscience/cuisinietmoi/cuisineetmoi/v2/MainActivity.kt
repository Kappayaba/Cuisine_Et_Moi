package com.starscience.cuisinietmoi.cuisineetmoi.v2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.ItemEditProfileBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment.AddRecipeFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment.FavoriteFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment.ProfileFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment.RecipeFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.EditProfileViewModel

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
    BottomNavigationView.OnNavigationItemReselectedListener, BubbleNavigationChangeListener {

    lateinit var binding: ItemEditProfileBinding
    lateinit var viewModel : EditProfileViewModel
    lateinit var bubble_nav : BubbleNavigationConstraintView
    lateinit var floating_button: FloatingActionButton

    //Android Initializer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Main)
        setContentView(R.layout.activity_main)

        floating_button = findViewById(R.id.floating_action_button_add_recipe)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RecipeFragment())
            .commit()

        bubble_nav = findViewById(R.id.bubble_nav)

        bubble_nav.setNavigationChangeListener(this)
        floating_button.setOnClickListener(this)
    }

    // Fragment, Activity Transition
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.favorites -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        FavoriteFragment()
                    )
                    .commit()
                floating_button.hide()
                return true
            }

            R.id.post -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        RecipeFragment()
                    )
                    .commit()
                floating_button.show()
                return true
            }

            R.id.profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        ProfileFragment()
                    )
                    .commit()
                floating_button.hide()
                return true
            }
        }
        return false
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.floating_action_button_add_recipe -> {
                var intent : Intent = Intent(this, AddRecipeFragment::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onNavigationItemReselected(p0: MenuItem) {
        return
    }

    fun hide_editProfile(v: View){
        viewModel.editFinish.value = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ProfileFragment()).commit()
    }

    fun show_editProfile(v: View){
        val parent = v.parent as ViewGroup
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_edit_profile, parent, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(this)).get(EditProfileViewModel::class.java)
        viewModel.editFinish.observe(this, Observer { value -> if(value == true) hide_editProfile(binding.root) })
        binding.viewModel = viewModel
        parent.removeAllViews()
        parent.addView(binding.root)
    }

    override fun onNavigationChanged(view: View?, position: Int) {
        when(position){
            0 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        FavoriteFragment()
                    )
                    .commit()
                floating_button.hide()
            }

            1 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        RecipeFragment()
                    )
                    .commit()
                floating_button.show()
            }

            2 -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,
                        ProfileFragment()
                    )
                    .commit()
                floating_button.hide()
            }
        }
    }
}
