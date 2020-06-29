package com.starscience.cuisinietmoi.cuisineetmoi.v2.utils


import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.sackcentury.shinebuttonlib.ShineButton
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("mutableImage")
fun setMutableImage(view: View, imageUrl: MutableLiveData<String>?){
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(parentActivity))
    if(parentActivity != null && imageUrl != null){
        imageUrl.observe(parentActivity, Observer { value -> if(value != "default")  ImageLoader.getInstance().displayImage(value, view as ImageView)  else ImageLoader.getInstance().displayImage("", view as ImageView)})
    }
}

@BindingAdapter("mutableCircleImage")
fun setMutableCircleImage(view: View, imageUrl: MutableLiveData<String>?){
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(parentActivity))
    if(parentActivity != null && imageUrl != null){
        imageUrl.observe(parentActivity, Observer { value -> ImageLoader.getInstance().displayImage(value, view as CircleImageView) })
    }
}

@BindingAdapter("mutableText")
fun setMutableText(view: TextView, text: MutableLiveData<String>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && text != null){
        text.observe(parentActivity, Observer { value -> view.text = value ?: "Alpha gyoza" })
    }
}

@BindingAdapter("mutableEditText")
fun setMutableEdit(view: EditText, text: MutableLiveData<String>?){
    val parentActivity = view.getParentActivity()
    if(parentActivity != null && text != null){
        text.observe(parentActivity, Observer { value -> view.setText(value, TextView.BufferType.EDITABLE) })
    }
}

@BindingAdapter("mutableErrorText")
fun setMutableError(view: View, text: MutableLiveData<String>?){
    val parentActivity = view.getParentActivity()
    if(parentActivity != null && text != null){
        text.observe(parentActivity, Observer { value -> if(value == null) (view as TextInputLayout).error = null else (view as TextInputLayout).error = value})
    }
}

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?){
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && visibility != null){
        visibility.observe(parentActivity, Observer { value -> view.visibility = value ?: View.GONE })
    }
}

@BindingAdapter("mutableAdapter")
fun setMutableAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>){
    view.adapter = adapter
}

@BindingAdapter("mutableChecked")
fun setMutableCheck(view: ShineButton, isChecked: MutableLiveData<Boolean>?){
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if(parentActivity != null && isChecked != null){
        isChecked.observe(parentActivity, Observer { value -> view.isChecked = value })
    }
}

@BindingAdapter("mutableRefreshing")
fun setSwipeRefresh(view: SwipeRefreshLayout, isRefreshing: MutableLiveData<Boolean>?){
    val parent = view.getParentActivity()
    if(parent != null && isRefreshing != null){
        isRefreshing.observe(parent, Observer { value -> view.isRefreshing = value })
    }
}

@BindingAdapter("mutableSwipeRefreshListener")
fun setListener(view: SwipeRefreshLayout, listener: SwipeRefreshLayout.OnRefreshListener){
    view.setOnRefreshListener(listener)
}
