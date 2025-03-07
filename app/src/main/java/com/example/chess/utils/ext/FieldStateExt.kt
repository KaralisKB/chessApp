package com.example.chess.utils.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.example.chess.R
import com.example.chess.local.model.FieldState

fun FieldState.getStateColor(): Int? = when (this) {
    FieldState.BLOCKED -> R.raw.red_blob
    FieldState.ATTACK -> R.raw.yellow_blob
    FieldState.VALID -> R.raw.green_blob
    FieldState.EMPTY -> null
}