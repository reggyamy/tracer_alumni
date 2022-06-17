package com.reggya.traceralumni

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.databinding.ActivityLandingBinding
import com.reggya.traceralumni.ui.viewmodel.SurveyViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory


class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preference = SurveyPreferences(this)
        if (preference.getCompleted()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView: View = window.decorView
            val wic = WindowInsetsControllerCompat(window, decorView)
            wic.isAppearanceLightStatusBars = true // true or false as desired.

            // And then you can set any background color to the status bar.
            window.statusBarColor = ContextCompat.getColor(this, R.color.blue)
        }


        ObjectAnimator.ofFloat(binding.imageView, "translationY", 100f).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        binding.btNext.setOnClickListener {
           isSurveyCompleted()
        }

    }

    private fun isSurveyCompleted() {
        val preference = SurveyPreferences(this)
        if (preference.getCompleted()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, SurveyActivity::class.java))
            finish()
        }
    }

}