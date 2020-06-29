package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentFavoritesBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.welcome_fragment.LoginActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.RecipeFavoriteListVIewModel

class FavoriteFragment : BaseFragment(), androidx.appcompat.widget.Toolbar.OnMenuItemClickListener {
    // Variables
    lateinit var binding: FragmentFavoritesBinding
    lateinit var viewModel: RecipeFavoriteListVIewModel
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        val view = binding.root

        binding.favoriteRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(RecipeFavoriteListVIewModel::class.java)
        viewModel.favRecipeList.observe(this, Observer { value -> Log.w("HAMZA WALLAH SOULED", "$value") })
        viewModel.clickedRecipe.observe(this, Observer { value -> startDetailActivity(value) })
        binding.viewModel = viewModel

        toolbar = view.findViewById(R.id.top_toolbar)
        toolbar.setOnMenuItemClickListener(this)

        return view
    }

    // Top toolbar item
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent: Intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
                return true
            }
        }
        return false
    }

}