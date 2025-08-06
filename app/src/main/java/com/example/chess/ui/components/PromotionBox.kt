package com.example.chess.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chess.local.model.Action
import com.example.chess.local.model.ActionType
import com.example.chess.local.model.BoardState
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.Position
import com.example.chess.ui.board.GameViewModel
import com.example.chess.ui.theme.LightMain
import com.example.chess.utils.Constants

@Composable
fun PromotionBox(
    modifier: Modifier = Modifier,
    selectedPiece: ChessPiece,
    state: BoardState,
    onAction: () -> Unit,
    position: Position,
    clickedSquare: Position,
    viewModel: GameViewModel
) {
    val onChosen: (ChessPiece) -> Unit = { piece ->

        viewModel.logAction(
            Action(
                turnId = viewModel.actionList.value.size + 1,
                gameId = 1, // TODO Make this gameId auto become the current games id from db
                selectedPiece,
                ActionType.PROMOTION,
                time = viewModel.initialTime?.minus((if(selectedPiece!!.color == PieceColor.WHITE) viewModel.whiteTimeRemaining else viewModel.blackTimeRemaining)!!) ?: 0,
                originalPosition = selectedPiece.position,
                newPosition = Position(clickedSquare.row, clickedSquare.col, FieldState.VALID),
                killedPiece = null,
                promotedToPiece = piece,
                castleIsLong = null,
                colorInCheck = null,
            )
        )

        state.board[clickedSquare.row][clickedSquare.col] = piece
        state.board[clickedSquare.row][clickedSquare.col]?.position =
            Position(clickedSquare.row, clickedSquare.col, FieldState.EMPTY)

        state.board[position.row][position.col] = null
        onAction()
    }

    val pieces = if (selectedPiece.color == PieceColor.WHITE) {
        Constants.getWhitePromotionPieces()
    } else {
        Constants.getBlackPromotionPieces()
    }
    Card(
        modifier = modifier
            .border(2.dp, Color.Black, shape = RoundedCornerShape(5.dp))
            .wrapContentHeight()
            .fillMaxWidth(0.8f)
            .background(LightMain)
    ) {
        Column(
            modifier = Modifier
                .background(LightMain)
                .padding(vertical = 25.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                modifier = Modifier
                    .background(LightMain)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                items(pieces) { element ->

                    Box(modifier = Modifier.clickable { onChosen(element) }) {
                        val infiniteTransition = rememberInfiniteTransition()
                        val shake by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = keyframes {
                                    durationMillis = 750
                                    1.1f at 500
                                }, repeatMode = RepeatMode.Restart
                            )
                        )
                        Image(
                            painter = painterResource(element.getImage()),
                            contentDescription = "Queen",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp)
                                .alpha(0.5f)
                                .scale(shake)
                        )
                        Image(
                            painter = painterResource(element.getImage()),
                            contentDescription = "Queen",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp)
                        )
                    }
                }

            }
        }
    }

}
