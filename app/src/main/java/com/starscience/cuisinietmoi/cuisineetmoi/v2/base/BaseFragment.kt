package com.starscience.cuisinietmoi.cuisineetmoi.v2.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.starscience.cuisinietmoi.cuisineetmoi.v2.DetailRecipeActivity
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.model.Recipe
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.ConnectionLiveData
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EXTRA_RECIPE

abstract class BaseFragment : Fragment(){
    protected lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectionLiveData = ConnectionLiveData(this.activity as AppCompatActivity)
    }

    fun startDetailActivity(recipe: Recipe?) {
        val intent = Intent(activity, DetailRecipeActivity::class.java)
            .putExtra(EXTRA_RECIPE, recipe)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.enter_down_to_up, R.anim.exit_down_to_up)
    }
}