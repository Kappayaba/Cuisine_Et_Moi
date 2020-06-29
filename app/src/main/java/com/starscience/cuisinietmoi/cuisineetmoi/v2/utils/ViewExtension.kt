package com.starscience.cuisinietmoi.cuisineetmoi.v2.utils

import android.app.Application
import android.content.*
import android.graphics.Canvas
import android.graphics.Paint
import android.net.ConnectivityManager
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.webkit.MimeTypeMap
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import com.starscience.cuisinietmoi.cuisineetmoi.v2.R
import java.lang.Exception


fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper){

        if(context is AppCompatActivity){
            return context
        }

        context = context.baseContext
    }
    return null
}

class DividerView : View{
    lateinit var paint: Paint

    constructor(context: Context) : this(context, null){
        paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.top_toolbar)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.top_toolbar)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs){
        paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.top_toolbar)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLine(0.toFloat(),24.toFloat(),20.toFloat(),24.toFloat(), paint)
        canvas?.drawLine(0.toFloat(), 0.toFloat(), 20.toFloat(), 0.toFloat(), paint)
    }
}

fun getFileExtensions(context: Context?, uri: Uri?) : String{
    return if(uri != null){
        val mipmapTypeMap = MimeTypeMap.getSingleton()
        mipmapTypeMap.getExtensionFromMimeType(context?.contentResolver?.getType(uri)).toString()
    }
    else{
        ""
    }
}

class ConnectionLiveData(private val context: Context) : LiveData<Boolean>(){

    private val networkReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            postValue(context?.isConnected)
        }
    }

    override fun onActive() {
        super.onActive()
        context.registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onInactive() {
        super.onInactive()
        try {
            context.unregisterReceiver(networkReceiver)
        }
        catch (e: Exception){ }
    }
}

val Context.isConnected: Boolean get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo?.isConnected == true