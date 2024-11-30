package com.example.hoverhopper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context) : SurfaceView(context), Runnable {

    private var isPlaying = false
    private var thread: Thread? = null
    private var surfaceHolder: SurfaceHolder = holder
    private var hopper: Hopper
    private val obstacles = mutableListOf<Obstacle>()
    private var score = 0
    private var scorePaint = Paint().apply {
        color = Color.BLACK
        textSize = 100f
        textAlign = Paint.Align.CENTER
    }
    private var isGameOver = false

    init {
        hopper = Hopper(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
        for(i in 0 until 3) {
            obstacles.add(
                Obstacle(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels)
                    .apply { x += i * 100 })
        }
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        //update the game objects(bird, pipe, etc)
        Log.d(GameView::class.simpleName, "**** update")
        hopper.update()
        obstacles.forEach {
            it.update()
            if (it.checkCollision(hopper.x, hopper.y, 50f)) {
                isGameOver = true
                stopGame()
            }
            if (!it.isScored && hopper.x > it.x + it.obstacleWidth) {
                score++
                it.isScored = true
            }
        }
    }

    private fun draw() {
        Log.d(GameView::class.simpleName, "**** draw")
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas = surfaceHolder.lockCanvas()
            //Draw background, pipes, bird, etc.
            canvas.drawColor(Color.CYAN)
            hopper.draw(canvas)
            obstacles.forEach { it.draw(canvas) }
            canvas.drawText("Score: $score",resources.displayMetrics.widthPixels / 2f, 200f, scorePaint )
            if (isGameOver) {
                val gameOverPaint = Paint().apply {
                    color = Color.RED
                    textSize = 150f
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText("Game Over", resources.displayMetrics.widthPixels / 2f, resources.displayMetrics.heightPixels / 2f, gameOverPaint)
            }
            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            hopper.hop()
        }
        return true
    }

    private fun sleep() {
        Log.d(GameView::class.simpleName, "**** sleep")
        try {
            Thread.sleep(16)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun startGame() {
        Log.d(GameView::class.simpleName, "**** start game")
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }

    fun stopGame() {
        Log.d(GameView::class.simpleName, "**** stop game")
        try {
            isPlaying = false
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}