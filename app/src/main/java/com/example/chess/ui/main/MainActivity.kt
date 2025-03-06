package com.example.chess.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.chess.ui.theme.ChessTheme
import com.example.chess.ui.board.Board
import com.example.chess.model.BoardState
import com.example.chess.ui.components.PieceColor


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChessTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    ChessApp()
                }
            }
        }
    }
}

@Preview
@Composable
fun ChessApp(modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        var turn by remember { mutableStateOf(PieceColor.WHITE) }
        val boardState = BoardState()
        Board(boardState)
    }

}

