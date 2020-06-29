package com.starscience.cuisinietmoi.cuisineetmoi.v2.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import com.starscience.cuisinietmoi.cuisineetmoi.v2.component.ViewModelFactory
import com.starscience.cuisinietmoi.cuisineetmoi.v2.databinding.FragmentLastPublishedBinding
import com.starscience.cuisinietmoi.cuisineetmoi.v2.ui.profile.LastPublishedViewModel
import kotlinx.android.synthetic.main.item_recipe.*

class LastPublished : Fragment() {
    lateinit var binding: FragmentLastPublishedBinding
    lateinit var viewModel: LastPublishedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.w("IOAZHNFIONA", "HAMZA")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_last_published, container, false)
        binding.lastPublishRecyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(activity as AppCompatActivity)).get(LastPublishedViewModel::class.java)
        binding.viewModel = viewModel

        val view = binding.root

        return view
    }

}