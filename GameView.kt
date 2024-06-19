package com.example.project5

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class GameView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val paint = Paint()
    private val pong = Pong()
    private var isPlaying = false
    private var score = 0
    private var bestScore = 0
    private var newBestScore = false
    private val handler = Handler(Looper.getMainLooper())
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PongGame", Context.MODE_PRIVATE)
    private val hitSound: MediaPlayer = MediaPlayer.create(context, R.raw.hit)

    init {
        bestScore = sharedPreferences.getInt("bestScore", 0)
        post {
            pong.resetBall(width)
            pong.resetPaddle(width, height)
            handler.postDelayed(updateRunnable, 16L)
        }
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (isPlaying) {
                pong.moveBall()
                pong.checkWallCollision(width)
                if (pong.ballY + pong.ballRadius >= pong.paddleY && pong.ballX >= pong.paddleX && pong.ballX <= pong.paddleX + pong.paddleWidth) {
                    pong.checkPaddleCollision()
                    hitSound.start()
                    score++
                    if (score > bestScore) {
                        bestScore = score
                        newBestScore = true
                    }
                }
                if (pong.ballY > height) {
                    isPlaying = false
                    saveBestScore()
                }
                invalidate()
            }
            handler.postDelayed(this, 16L)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.RED
        canvas.drawCircle(pong.ballX.toFloat(), pong.ballY.toFloat(), pong.ballRadius.toFloat(), paint)
        paint.color = Color.BLUE
        canvas.drawRect(
            pong.paddleX.toFloat(),
            pong.paddleY.toFloat(),
            (pong.paddleX + pong.paddleWidth).toFloat(),
            (pong.paddleY + pong.paddleHeight).toFloat(),
            paint
        )

        if (!isPlaying) {
            paint.color = Color.BLACK
            paint.textSize = 50f
            val scoreText = "Score: $score"
            val bestScoreText = "Best Score: $bestScore"
            canvas.drawText(scoreText, 50f, height / 2f, paint)
            canvas.drawText(bestScoreText, 50f, height / 2f + 60, paint)
            if (newBestScore) {
                canvas.drawText("New Best Score!", 50f, height / 2f + 120, paint)
            }
        }
    }

    private fun saveBestScore() {
        sharedPreferences.edit().putInt("bestScore", bestScore).apply()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!isPlaying) {
                    score = 0
                    newBestScore = false
                    isPlaying = true
                    pong.resetBall(width)
                    pong.resetPaddle(width, height)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                pong.paddleX = (event.x - pong.paddleWidth / 2).toInt()
                if (pong.paddleX < 0) pong.paddleX = 0
                if (pong.paddleX + pong.paddleWidth > width) pong.paddleX = width - pong.paddleWidth
            }
        }
        return true
    }
}
