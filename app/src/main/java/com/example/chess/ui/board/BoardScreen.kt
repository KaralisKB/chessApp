package com.example.chess.ui.board

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chess.R
import com.example.chess.local.model.BoardState
import com.example.chess.ui.components.ChessLottie
import com.example.chess.ui.components.ChessPiece
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.Pawn
import com.example.chess.ui.components.PieceColor
import com.example.chess.local.model.Position
import com.example.chess.ui.components.ChessLazyHorizontalGrid
import com.example.chess.ui.components.Piece
import com.example.chess.ui.components.PromotionBox
import com.example.chess.ui.theme.Jade
import com.example.chess.utils.ext.getStateColor
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chess.ui.components.BlobLottie


@Composable
fun BoardScreen(viewModel: BoardViewModel = viewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top
        ) {
            ChessLottie(modifier = Modifier
                    .size(225.dp)
                    .align(Alignment.CenterHorizontally), id = R.raw.chess_knight,1f)
            Spacer(modifier = Modifier.height(20.dp))
            ChessLazyHorizontalGrid(viewModel.board.killedWhitePieces)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
            ) {
                Column(
                    modifier = Modifier.weight(2f), verticalArrangement = Arrangement.Center
                ) {
                    for (row in 7 downTo 0) {
                        BoardLabel(
                            modifier = Modifier.weight(1f), text = (row + 1).toString()
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
                                        viewModel.board,
                                        viewModel.selectedPiece,
                                        onSquareClick = { viewModel.handleSquareClick(row, col) },
                                        viewModel.possibleMoves
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(2f))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Row(Modifier.weight(8f)) {
                    for (col in 0..7) {
                        BoardLabel(
                            text = ('A' + col).toString(), modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(20.dp))
            ChessLazyHorizontalGrid(viewModel.board.killedBlackPieces)

            Spacer(modifier = Modifier.height(20.dp))
            ChessLottie(
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally),
                id = R.raw.trophy,
                1.5f
            )
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = viewModel.isPromotionPossible,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 500)
            ) + fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
                animationSpec = tween(durationMillis = 500),
            ) + fadeOut(animationSpec = tween(durationMillis = 500))
        ) {
            val promotionCandidate = if (viewModel.selectedPiece != null) viewModel.selectedPiece else Pawn(
                PieceColor.WHITE,
                Position(0, 0, FieldState.EMPTY)
            )
            PromotionBox(
                modifier = Modifier.padding(top = 25.dp),
                promotionCandidate!!,
                viewModel.board,
                onAction = viewModel.onPromotionGranted,
                position = promotionCandidate.position,
                viewModel.clickedSquare!!
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
    val piece = boardState.board[row][column]
    val scale = remember { Animatable(1f) }

    val movePosition = possibleMoves?.firstOrNull { it.row == row && it.col == column }
    val moveAnimation = movePosition?.type?.getStateColor()

    LaunchedEffect(highlightedPiece) {
        if (piece == highlightedPiece) {
            scale.animateTo(
                targetValue = 1.2f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            scale.animateTo(
                targetValue = 1f, animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(if ((row + column) % 2 == 0) Jade else Color.White)
            .clickable { onSquareClick() }
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
    ) {
        if (moveAnimation != null) {
            BlobLottie(Modifier, movePosition.type.getStateColor()!!, 1f)
        }

        if (piece != null) {
            Piece(piece)
        }
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
private fun BoardPreview() {
    BoardScreen()
}