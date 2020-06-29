package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.main_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.starscience.cuisinietmoi.cuisineetmoi.v2.DetailRecipeActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.base.BaseFragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentRecipesBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.welcome_fragment.LoginActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.recipe.RecipeListViewModel
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EXTRA_RECIPE
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.isConnected

class RecipeFragment : BaseFragment(), Toolbar.OnMenuItemClickListener {
    // Variables
    private lateinit var toolbar: Toolbar
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var viewModel: RecipeListViewModel

    private var snackbar: Snackbar? = null

    //Android Initializer
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_recipes, container, false)
        val view = binding.root

        binding.recipeRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(RecipeListViewModel::class.java)

        connectionLiveData.observe(this, Observer {
            viewModel.isNetworkAvailable.value = it
        })
        viewModel.isNetworkAvailable.value = (activity as AppCompatActivity).isConnected

        viewModel.errorMessage.observe(this, Observer { errorMessage -> if(errorMessage != null) showError(errorMessage) else hideError()})
        viewModel.clickedRecipe.observe(this    , Observer { recipe -> if(recipe != null) startDetailActivity(recipe) })
        binding.viewModel = viewModel

        toolbar = view.findViewById(R.id.top_toolbar)
        toolbar.setOnMenuItemClickListener(this)

        return view
    }

    private fun hideError() {
        snackbar?.dismiss()
    }

    private fun showError(@StringRes errorMessage: Int) {
        snackbar = Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT)
        snackbar?.setAction(R.string.retry, viewModel.errorClickListener)
        snackbar?.show()
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