package com.example.chess.utils.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Yellow
import com.example.chess.local.model.FieldState

fun FieldState.getStateColor(): Color? = when (this) {
    FieldState.BLOCKED -> Red
    FieldState.ATTACK -> Yellow
    FieldState.VALID -> Green
    FieldState.EMPTY -> null
}