package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.reggya.traceralumni.databinding.ActivitySplashScreenBinding
import com.reggya.traceralumni.ui.ConnectionLiveData

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appName = arrayOf("T","r","a","c","e","r"," ","A","l","u","m","n","i" )

        for (i in 0..12){
            Handler(Looper.getMainLooper()).postDelayed({ binding.appName.append(appName[i]) },
                (300+(i*200)).toLong()
            )
        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)

    }
}