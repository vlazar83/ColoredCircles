package com.vlazar83.coloredcircles

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import kotlinx.coroutines.*


class Timer {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private fun startCoroutineTimer(delayMillis: Long = 0, repeatMillis: Long = 0, action: () -> Unit) = scope.launch(Dispatchers.IO) {
        delay(delayMillis)
        if (repeatMillis > 0) {
            while (true) {
                action()
                delay(repeatMillis)
            }
        } else {
            action()
        }
    }

    private val timer: Job = startCoroutineTimer(delayMillis = 0, repeatMillis = 10000) {
        Log.d(TAG, "Background - tick")
        //doSomethingBackground()
        scope.launch(Dispatchers.Main) {
            Log.d(TAG, "Main thread - tick")
            //doSomethingMainThread()
        }
    }

    fun startTimer() {
        timer.start()
    }

    fun cancelTimer() {
        timer.cancel()
    }

    fun startSound(context: Context){
        var mediaPlayer: MediaPlayer? = MediaPlayer.create(context, R.raw.beeps)
        mediaPlayer?.start()
    }

}