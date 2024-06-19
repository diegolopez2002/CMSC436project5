package com.example.project5

import android.os.Handler
import android.os.Looper

class GameTimerTask(private val updateRunnable: Runnable) {
    private val handler = Handler(Looper.getMainLooper())

    fun start() {
        handler.postDelayed(updateRunnable, 16L)
    }

    fun stop() {
        handler.removeCallbacks(updateRunnable)
    }
}
