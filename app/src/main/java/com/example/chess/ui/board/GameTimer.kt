package com.example.chess.ui.board

object GameTimer {
    val startTimeMillis: Long = System.currentTimeMillis()

    fun getElapsedTime(eventTimeMillis: Long): String {
        val elapsed = eventTimeMillis - startTimeMillis
        val seconds = (elapsed / 1000) % 60
        val minutes = (elapsed / 1000) / 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}