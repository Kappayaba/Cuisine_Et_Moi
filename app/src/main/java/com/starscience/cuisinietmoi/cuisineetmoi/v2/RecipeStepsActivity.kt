package com.starscience.cuisinietmoi.cuisineetmoi.v2

import android.animation.ArgbEvaluator
import android.app.Activity
import android.app.VoiceInteractor
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.graphics.red
import androidx.viewpager.widget.ViewPager
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.EXTRA_STEPS
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.REQ_CODE_SPEECH_INPUT
import com.starscience.cuisinietmoi.cuisineetmoi.v2.utils.RecipeStepAdapter
import java.util.*
import java.util.jar.Manifest
import kotlin.properties.Delegates

class RecipeStepsActivity : AppCompatActivity(), ViewPager.OnPageChangeListener {

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    private lateinit var steps: List<String>
    private lateinit var adapter: RecipeStepAdapter
    private lateinit var viewPager: ViewPager
    private var random = Random()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_steps)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), PackageManager.PERMISSION_GRANTED)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        steps = intent.getStringArrayListExtra(EXTRA_STEPS)
        Log.w("BRUH", "$steps")

        viewPager = findViewById(R.id.steps_viewpager)
        adapter = RecipeStepAdapter(steps, this)

        viewPager.adapter = adapter
        viewPager.setPadding(0, 0,0,0)

        viewPager.addOnPageChangeListener(this)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().displayLanguage)

        speechRecognizer.setRecognitionListener(object : RecognitionListener{
            override fun onReadyForSpeech(params: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onRmsChanged(rmsdB: Float) {
                TODO("Not yet implemented")
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                TODO("Not yet implemented")
            }

            override fun onPartialResults(partialResults: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onBeginningOfSpeech() {
                TODO("Not yet implemented")
            }

            override fun onEndOfSpeech() {
                TODO("Not yet implemented")
            }

            override fun onError(error: Int) {
                TODO("Not yet implemented")
            }

            override fun onResults(results: Bundle?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onPageScrollStateChanged(state: Int) {
        return
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        return
    }

    override fun onPageSelected(position: Int) {
        return
    }
}
