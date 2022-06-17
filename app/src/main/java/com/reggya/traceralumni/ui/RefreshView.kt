package com.reggya.traceralumni.ui

import android.os.Handler
import android.os.Looper

class RefreshView {
    fun refresh(time: Long){
        val handler = Handler(Looper.getMainLooper())
        val refresh: Runnable = object : Runnable {
            override fun run() {
                // data request
                handler.postDelayed(this, time)
            }
        }
        handler.postDelayed(refresh, time)
    }

}