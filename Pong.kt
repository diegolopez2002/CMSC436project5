package com.example.project5

class Pong {
    var ballX: Int = 0
    var ballY: Int = 0
    var ballRadius: Int = 20
    var ballSpeedX: Int = 10
    var ballSpeedY: Int = 10

    var paddleX: Int = 0
    var paddleY: Int = 0
    var paddleWidth: Int = 200
    var paddleHeight: Int = 30

    fun moveBall() {
        ballX += ballSpeedX
        ballY += ballSpeedY
    }

    fun checkWallCollision(width: Int) {
        if (ballX <= ballRadius || ballX >= width - ballRadius) {
            ballSpeedX = -ballSpeedX
        }

        if (ballY <= ballRadius) {
            ballSpeedY = -ballSpeedY
        }
    }

    fun checkPaddleCollision() {
        ballSpeedY = -ballSpeedY
    }

    fun resetBall(width: Int) {
        ballX = width / 2
        ballY = ballRadius + 20
        ballSpeedY = Math.abs(ballSpeedY) // Ensure the ball moves downwards initially
    }

    fun resetPaddle(width: Int, height: Int) {
        paddleX = (width - paddleWidth) / 2
        paddleY = height - paddleHeight - 30
    }
}
