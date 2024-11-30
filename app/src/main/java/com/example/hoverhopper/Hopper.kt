package com.example.hoverhopper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Hopper(private val screenWidth: Int, private val screenHeight: Int) {

    var x = screenWidth / 4f
    var y = screenHeight / 2f
    var velocity = 0f
    private val gravity = 0.5f
    private val lift = -10f
    private val paint = Paint()

    init {
        paint.color = Color.YELLOW
    }

    fun update() {
        velocity += gravity
        y += velocity

        if (y <0) y = 0f
        if (y > screenHeight) y = screenHeight.toFloat()
    }

    fun hop() {
        velocity = lift
    }

    fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, 50f, paint)
    }
}