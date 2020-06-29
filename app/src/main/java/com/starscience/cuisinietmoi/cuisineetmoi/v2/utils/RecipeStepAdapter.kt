package com.starscience.cuisinietmoi.cuisineetmoi.v2.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R

class RecipeStepAdapter(private val steps: List<String>, private val context: Context) : PagerAdapter() {

    private lateinit var layoutInflater: LayoutInflater

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return steps.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_recipe_steps, container, false)

        val stepsTextView: TextView = view.findViewById(R.id.step_text)
        stepsTextView.text = steps[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}