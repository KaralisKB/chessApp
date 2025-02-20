package com.example.chess.model

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chess.R


@Composable
fun Board(state: BoardState) {
    var selectedPiece by rememberSaveable { mutableStateOf<ChessPiece?>(null) }
    var possibleMoves by rememberSaveable { mutableStateOf<List<Position>?>(null) }

    fun handleSquareClick(row: Int, col: Int) {
        val clickedPiece = state.board[row][col]
        selectedPiece = if (clickedPiece != null) {
            clickedPiece
        } else {
            null
        }
        possibleMoves = selectedPiece?.getPossibleMoves(state, null)

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center)
        ) {
            for (row in 7 downTo 0) {
                Row {
                    BoardLabel(text = (row + 1).toString())
                    for (col in 0..7) {
                        GridItem(
                            row,
                            col,
                            state,
                            selectedPiece,
                            onSquareClick = { handleSquareClick(row, col)},
                            possibleMoves
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.size(20.dp))
                for (col in 0..7) {
                    BoardLabel(text = ('A' + col).toString())
                }
            }
        }
    }
}

@Composable
fun GridItem(
    row: Int,
    column: Int,
    boardState: BoardState,
    highlightedPiece: ChessPiece?,
    onSquareClick: () -> Unit,
    possibleMoves: List<Position>?
) {
    var piece = boardState.board[row][column]
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                if (piece != null && piece == highlightedPiece) Color.DarkGray
                else if (possibleMoves != null && possibleMoves.contains( Position(row, column, FieldState.VALID))) Color.Green
                else if (possibleMoves != null && possibleMoves.contains(Position(row, column, FieldState.ATTACK))) Color.Yellow
                else if (possibleMoves != null && possibleMoves.contains( Position(row, column, FieldState.BLOCKED))) Color.Red
                else if ((row + column) % 2 == 0) Color.LightGray
                else Color.White
            )
            .clickable { onSquareClick() }
    ) {
        if (piece != null) {
            Piece(piece)
        }
    }
}

@Composable
fun backgroundBox() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(Color.Gray)
    )
}


@Composable
fun Piece(piece: ChessPiece) {
    val imageId = when (piece.type) {
        PieceType.PAWN -> if (piece.color == PieceColor.WHITE) R.drawable.chess_plt60 else R.drawable.chess_pdt60
        PieceType.KNIGHT -> if (piece.color == PieceColor.WHITE) R.drawable.chess_nlt60 else R.drawable.chess_ndt60
        PieceType.BISHOP -> if (piece.color == PieceColor.WHITE) R.drawable.chess_blt60 else R.drawable.chess_bdt60
        PieceType.ROOK -> if (piece.color == PieceColor.WHITE) R.drawable.chess_rlt60 else R.drawable.chess_rdt60
        PieceType.QUEEN -> if (piece.color == PieceColor.WHITE) R.drawable.chess_qlt60 else R.drawable.chess_qdt60
        PieceType.KING -> if (piece.color == PieceColor.WHITE) R.drawable.chess_klt60 else R.drawable.chess_kdt60
        else -> null
    }

    if (imageId != null) {
        Box {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = piece.type.toString(),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(38.dp)
            )
        }
    } else {
        Text("?", color = if (piece.color == PieceColor.WHITE) Color.White else Color.Black)
    }
}

@Composable
fun BoardLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(4.dp)
            .padding(horizontal = 7.dp)
            .size(20.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}