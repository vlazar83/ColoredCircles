package com.vlazar83.coloredcircles

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.Color.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.*

class MainActivity : AppCompatActivity() {
    protected lateinit var frameLayout: ViewGroup
    protected var DEFAULT_ANIMATION_DURATION = 2500L
    val colorsAlreadyTaken = mutableSetOf(-1)
    val random = Random()
    var generatedCircles = 0

    fun rand(from: Int, to: Int) : Int {
        var randomNum = -100
        do {
            randomNum = random.nextInt(to - from) + from
        } while (colorsAlreadyTaken.contains(randomNum))
        colorsAlreadyTaken.add(randomNum)
        return randomNum
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.container)
        frameLayout.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action){
                MotionEvent.ACTION_DOWN -> {

                    // generate up to 10 circles max
                    if(generatedCircles<10){
                        var generatedColor = Color.BLACK
                        when (rand(0,10)) {
                            0 -> generatedColor = Color.BLACK
                            1 -> generatedColor = Color.DKGRAY
                            2 -> generatedColor = Color.GRAY
                            3 -> generatedColor = Color.LTGRAY
                            4 -> generatedColor = Color.RED
                            5 -> generatedColor = Color.GREEN
                            6 -> generatedColor = Color.BLUE
                            7 -> generatedColor = Color.YELLOW
                            8 -> generatedColor = Color.CYAN
                            9 -> generatedColor = Color.MAGENTA
                        }
                        val coloredCircles = ColoredCircles( this, null, motionEvent.getX(), motionEvent.getY(), generatedColor)
                        generatedCircles++
                        frameLayout.addView(coloredCircles)
                        onStartAnimation(coloredCircles)
                    }
                }
                MotionEvent.ACTION_UP -> {

                }
            }
            return@OnTouchListener true
        })

    }

    protected fun onStartAnimation(coloredCircles: ColoredCircles){

        val alphaAnimator = ValueAnimator.ofFloat(0.4f, 1f)
        alphaAnimator.repeatMode = ValueAnimator.REVERSE
        alphaAnimator.repeatCount = 10

        alphaAnimator.addUpdateListener {
            val value = it.animatedValue as Float
            coloredCircles.alpha = value
        }

        alphaAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {

            }

            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationRepeat(animation: Animator) {}
        })

        val animatorSet = AnimatorSet()

        animatorSet.play(alphaAnimator)
        animatorSet.duration = DEFAULT_ANIMATION_DURATION
        animatorSet.start()
    }

}
