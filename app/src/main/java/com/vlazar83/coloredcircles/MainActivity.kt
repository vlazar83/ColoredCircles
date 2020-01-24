package com.vlazar83.coloredcircles

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import java.util.*

class MainActivity : AppCompatActivity() {
    protected lateinit var frameLayout: ViewGroup
    protected var DEFAULT_ANIMATION_DURATION = 2500L
    val randomNumbersAlreadyTaken = mutableSetOf(-1)
    val generatedCircleViews: MutableList<ColoredCircles> = mutableListOf<ColoredCircles>()
    val random = Random()
    var generatedCircles = 0
    var lastRandomNumber = -1
    var timerStarted = false
    var startTimer = false

    fun rand(from: Int, to: Int) : Int {
        var randomNum = -100
        do {
            randomNum = random.nextInt(to - from) + from
        } while (randomNumbersAlreadyTaken.contains(randomNum))
        randomNumbersAlreadyTaken.add(randomNum)
        lastRandomNumber = randomNum
        return randomNum
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frameLayout = findViewById(R.id.container)
        frameLayout.setBackgroundColor(Color.BLACK)
        frameLayout.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            startTimer = true
            handleTouch(motionEvent)
            true
            return@OnTouchListener true
        })

    }

    private var mActivePointerId: Int = 0

    private fun handleTouch(m: MotionEvent)
    {
        if(startTimer && !timerStarted){
            timerStarted = true
            startTimerAndSound()
        }
        val pointerCount = m.pointerCount

        for (i in 0 until pointerCount)
        {
            mActivePointerId = m.getPointerId(i)

            val (x: Float, y: Float) = m.findPointerIndex(mActivePointerId).let { pointerIndex ->
                // Get the pointer's current position
                m.getX(pointerIndex) to m.getY(pointerIndex)
            }

            val action = m.actionMasked
            val actionIndex = m.actionIndex

            when (action)
            {
                MotionEvent.ACTION_DOWN -> doTheDrawing(x,y)
                //MotionEvent.ACTION_UP -> eraseCircle(m)
                MotionEvent.ACTION_POINTER_DOWN -> doTheDrawing(x,y)
                //MotionEvent.ACTION_POINTER_UP -> eraseCircle(m)
                //MotionEvent.ACTION_MOVE -> doTheDrawing(x,y)
            }
        }

    }

    private fun startTimerAndSound(){
        var timer = Timer()
        timer.startTimer()
        timer.startSound(frameLayout.context)
    }

    fun eraseCircle(motionEvent:MotionEvent){
        // search for the circle which needs to be erased upon releasing the touch (20 px tolerance)
        for(circle in generatedCircleViews){
            if( (circle.xPositionForCircle + 20 >= motionEvent.getX() && circle.xPositionForCircle - 20 <= motionEvent.getX()) &&
                (circle.yPositionForCircle + 20 >= motionEvent.getY() && circle.yPositionForCircle - 20 <= motionEvent.getY())){
                circle.clearAnimation()
                frameLayout.removeView(circle)
                randomNumbersAlreadyTaken.remove(circle.circleColorRandomNumber)
                generatedCircles--
                generatedCircleViews.remove(circle)
                break
            }
        }
    }

    fun doTheDrawing(x:Float, y:Float){
        // generate up to 8 circles max
        if(generatedCircles<8){
            var generatedColor = Color.WHITE
            when (rand(0,8)) {
                0 -> generatedColor = Color.WHITE
                1 -> generatedColor = Color.DKGRAY
                2 -> generatedColor = Color.RED
                3 -> generatedColor = Color.GREEN
                4 -> generatedColor = Color.BLUE
                5 -> generatedColor = Color.YELLOW
                6 -> generatedColor = Color.CYAN
                7 -> generatedColor = Color.MAGENTA
            }
            val coloredCircles = ColoredCircles( this, null, x, y, generatedColor, lastRandomNumber)
            generatedCircleViews.add(coloredCircles)
            generatedCircles++
            frameLayout.addView(coloredCircles)

            val sizeAnimator = ScaleAnimation(0.8f, 1.1f, 0.8f, 1.1f, Animation.ABSOLUTE, x, Animation.ABSOLUTE, y)
            sizeAnimator.setFillAfter(true)
            sizeAnimator.repeatMode = ValueAnimator.REVERSE
            sizeAnimator.repeatCount = 10
            sizeAnimator.setDuration(1000)

            coloredCircles.startAnimation(sizeAnimator).apply {  }


            onStartAnimation(coloredCircles)
        }
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
