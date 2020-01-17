package com.vlazar83.coloredcircles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt

class ColoredCircles(context: Context?, attrs: AttributeSet?, xPositionForCircle: Float, yPositionForCircle: Float, color: Int) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var circleColor = color
    private var borderColor = Color.BLACK
    private var borderWidth = 4.0f
    private var size = 150
    private var xPositionForCircle = xPositionForCircle
    private var yPositionForCircle = yPositionForCircle

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircle(xPositionForCircle, yPositionForCircle, canvas, circleColor)
    }

    private fun drawCircle(x: Float, y: Float, canvas: Canvas, color: Int) {

        paint.color = color
        paint.style = Paint.Style.FILL

        val radius = size / 2f
        canvas.drawCircle(x, y, radius, paint)

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        canvas.drawCircle(x, y, radius - borderWidth / 2f, paint)
    }

}