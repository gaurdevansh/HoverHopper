package com.example.hoverhopper

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.random.Random

class Obstacle(private val screenWidth: Int, private val screenHeight: Int) {

    val obstacleWidth = 200f
    private val gapHeight = 500f
    var x = screenWidth.toFloat() //Start at the right edge of screen
    private var topObstacleHeight = Random.nextInt(200, (screenHeight - 200 - gapHeight).toInt())
    private val paint = Paint()
    var isScored = false

    init {
        paint.color = Color.GREEN
    }

    fun update() {
        x -= 10 //Move pipe leftwards
        if (x + obstacleWidth < 0) { //If obstacle moves off screen
            x = screenWidth.toFloat()
            val newTopHeight = Random.nextInt(200, (screenHeight - 200 - gapHeight).toInt())
            topObstacleHeight = newTopHeight
            isScored = false
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawRect(x, 0f, x+obstacleWidth, topObstacleHeight.toFloat(), paint)
        canvas.drawRect(x, topObstacleHeight + gapHeight, x + obstacleWidth, screenHeight.toFloat(), paint)
    }

    fun checkCollision(hopperX: Float, hopperY: Float, hopperRadius: Float): Boolean {
        if (hopperX + hopperRadius > x && hopperX - hopperRadius < x + obstacleWidth) {
            if (hopperY - hopperRadius < topObstacleHeight || hopperRadius > topObstacleHeight + gapHeight) {
                return true
            }
        }
        return false
    }

}