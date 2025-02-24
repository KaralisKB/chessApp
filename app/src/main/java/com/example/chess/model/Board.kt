package com.example.chess.model

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chess.R
import com.example.chess.utils.ext.getStateColor


@Composable
fun Board(initialState: BoardState) {
    var state by remember { mutableStateOf(initialState) }
    var selectedPiece by rememberSaveable { mutableStateOf<ChessPiece?>(null) }
    var possibleMoves by rememberSaveable { mutableStateOf<List<Position>?>(null) }

    fun handleSquareClick(row: Int, col: Int) {
        val clickedPiece = state.board[row][col]

        if (selectedPiece != null && clickedPiece == null && possibleMoves?.contains(Position(row, col, FieldState.VALID)) == true) {
            state.move(selectedPiece!!, Position(row, col, FieldState.VALID))
            possibleMoves = selectedPiece?.getPossibleMoves(state, null)
        }
        else if (selectedPiece != null && clickedPiece != null && possibleMoves?.contains(Position(row, col, FieldState.ATTACK)) == true) {
            state.attack(selectedPiece!!,Position(row, col, FieldState.ATTACK))
            possibleMoves = selectedPiece?.getPossibleMoves(state, null)
        }
        else {
            selectedPiece = clickedPiece
            possibleMoves = selectedPiece?.getPossibleMoves(state, null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Top
        ) {
            ChessLottie(
                modifier = Modifier
                    .size(225.dp)
                    .align(Alignment.CenterHorizontally),
                id = R.raw.chess_knight,
                1f
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                Column(
                    modifier = Modifier
                        .weight(2f),
                    verticalArrangement = Arrangement.Center
                ) {
                    for (row in 7 downTo 0) {
                        BoardLabel(
                            modifier = Modifier.weight(1f),
                            text = (row + 1).toString()
                        )
                    }
                }
                Box(modifier = Modifier.border(1.dp, Color.Black)) {
                    Column {
                        for (row in 7 downTo 0) {
                            Row {
                                for (col in 0..7) {
                                    GridItem(
                                        row,
                                        col,
                                        state,
                                        selectedPiece,
                                        onSquareClick = { handleSquareClick(row, col) },
                                        possibleMoves
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(2f))
            }

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)) {
                Spacer(modifier = Modifier.weight(1f))

                Row(Modifier.weight(8f)) {
                    for (col in 0..7) {
                        BoardLabel(
                            text = ('A' + col).toString(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(20.dp))
            ChessLottie(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally),
                id = R.raw.trophy,
                1.5f
            )
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
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }

    LaunchedEffect(highlightedPiece) {
        if (piece == highlightedPiece) {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(
                when {
                    //TODO: have this not be broken p.2
                    piece != null && piece == highlightedPiece -> Color.DarkGray
                    possibleMoves != null && possibleMoves.any{it.row == row && it.col == column} -> Position(row, column, possibleMoves.first{ it.row == row && it.col == column }.type).type.getStateColor() ?:
                    if ((row + column) % 2 == 0) Color.LightGray else Color.White
                    else -> if ((row + column) % 2 == 0) Color.LightGray else Color.White
                }
            )
            .clickable { onSquareClick() }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        if (piece != null) {
            Piece(piece)
        }
    }
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
fun BoardLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        textAlign = TextAlign.Center,
        text = text,
        modifier = modifier
            .wrapContentHeight(Alignment.CenterVertically)
            .fillMaxWidth()
    )
}



@Preview
@Composable
fun BoardPreview() {
    Board(BoardState())
}