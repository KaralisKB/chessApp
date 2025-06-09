package com.example.chess.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle

fun blurEffect(backgroundColor: Color): HazeStyle = HazeStyle(
    backgroundColor = backgroundColor,
    tint = null,
    blurRadius = 2.dp,
    noiseFactor = 0.1f
)