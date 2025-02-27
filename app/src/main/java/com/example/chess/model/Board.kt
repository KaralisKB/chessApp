package com.example.chess.model

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.chess.R
import com.example.chess.ui.components.ChessLazyHorizontalGrid
import com.example.chess.ui.components.Piece
import com.example.chess.ui.components.PromotionBox
import com.example.chess.ui.theme.Jade
import com.example.chess.utils.ext.getStateColor


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Board(initialState: BoardState) {
    val state by remember { mutableStateOf(initialState) }
    var selectedPiece by rememberSaveable { mutableStateOf<ChessPiece?>(null) }
    var possibleMoves by rememberSaveable { mutableStateOf<List<Position>?>(null) }
    var isPromotionPossible by remember {mutableStateOf(false)}
    var clickedSquare by remember {mutableStateOf<Position?>(null)}
    val onPromotionGranted: () -> Unit = {
        selectedPiece = null
        possibleMoves = null
        isPromotionPossible = false
    }

    fun handleSquareClick(row: Int, col: Int) {
        clickedSquare = Position(row, col, FieldState.EMPTY)
        val clickedPiece = state.board[row][col]

        if (selectedPiece?.type == PieceType.PAWN && selectedPiece?.movesMade!! >= 4  && (clickedSquare!!.row == 0 || clickedSquare!!.row == 7)){
            if (selectedPiece?.color == PieceColor.WHITE && selectedPiece?.position?.row == 6){
                isPromotionPossible = true
            }else if (selectedPiece?.color == PieceColor.BLACK && selectedPiece?.position?.row == 1){
                isPromotionPossible = true
            }
        } else {
            if (selectedPiece != null &&
                clickedPiece == null &&
                possibleMoves?.contains(Position(row, col, FieldState.VALID)) == true)
            {
                state.move(selectedPiece!!, Position(row, col, FieldState.VALID))
                possibleMoves = selectedPiece?.getPossibleMoves(state, null)
                isPromotionPossible = false
            }
            else if (selectedPiece != null &&
                clickedPiece != null &&
                possibleMoves?.contains(Position(row, col, FieldState.ATTACK)) == true
            ) {
                state.attack(selectedPiece!!, Position(row, col, FieldState.ATTACK))
                possibleMoves = selectedPiece?.getPossibleMoves(state, null)
                isPromotionPossible = false
            } else {
                selectedPiece = clickedPiece
                possibleMoves = selectedPiece?.getPossibleMoves(state, null)
                isPromotionPossible = false
            }
        }
    }
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.Top

        ) {

                AnimatedVisibility(
                    visible = isPromotionPossible,
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(durationMillis = 1500)
                    ) + fadeIn(animationSpec = tween(durationMillis = 1500)),
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> -fullHeight },
                        animationSpec = tween(durationMillis = 500)
                    ) + fadeOut(animationSpec = tween(durationMillis = 500))
                ) {
                    Box(modifier = Modifier.fillMaxSize().zIndex(0f).align(Alignment.CenterHorizontally), contentAlignment = Alignment.Center) {
                        val promotionCandidate = if (selectedPiece != null) selectedPiece else Pawn(PieceColor.WHITE, Position(0,0,FieldState.EMPTY))
                        PromotionBox(
                            modifier = Modifier
                                .padding(top = 25.dp),
                            promotionCandidate!!,
                            state,
                            onAction = onPromotionGranted,
                            position = promotionCandidate.position,
                            isPromotionPossible
                        )
                    }
            }
            if(!isPromotionPossible) {
                ChessLottie(
                    modifier = Modifier
                        .size(225.dp)
                        .align(Alignment.CenterHorizontally),
                    id = R.raw.chess_knight,
                    1f
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            ChessLazyHorizontalGrid(state.killedWhitePieces)
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
            ChessLazyHorizontalGrid(state.killedBlackPieces)

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
    val piece = boardState.board[row][column]
    val scale = remember { Animatable(1f) }

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
    Box(modifier = Modifier
        .size(40.dp)
        .background(when {
            piece != null && piece == highlightedPiece -> Color.DarkGray
            possibleMoves != null && possibleMoves.any { it.row == row && it.col == column } ->
                Position(
                    row,
                    column,
                    possibleMoves.first { it.row == row && it.col == column }
                        .type).type.getStateColor()
                    ?: if ((row + column) % 2 == 0) Jade else Color.White

            else -> if ((row + column) % 2 == 0) Jade else Color.White
        }
        )
        .clickable { onSquareClick() }
        .graphicsLayer {
            scaleX = scale.value
            scaleY = scale.value
        }) {
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
fun BoardPreview() {
    Board(BoardState())
}