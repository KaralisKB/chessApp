package com.example.chess.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.zIndex
import com.example.chess.model.BoardState
import com.example.chess.model.ChessPiece
import com.example.chess.model.FieldState
import com.example.chess.model.PieceColor
import com.example.chess.model.Position
import com.example.chess.ui.theme.Jade
import com.example.chess.ui.theme.transparentGray
import com.example.chess.utils.Constants

@Composable
fun PromotionBox(
    modifier: Modifier = Modifier,
    selectedPiece: ChessPiece,
    state: BoardState,
    onAction: () -> Unit,
    position: Position,
    isPromotionPossible: Boolean
) {
        val direction = if (selectedPiece.color == PieceColor.WHITE) +1 else -1

        val onChosen: (ChessPiece) -> Unit = { piece ->
            state.board[position.row + direction][position.col] = piece
            state.board[position.row + direction][position.col]?.position =
                Position(position.row + direction, position.col, FieldState.EMPTY)

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
                    .background(Jade)
                ) {
                Column(
                    modifier = Modifier
                        .background(Jade)
                        .padding(vertical = 25.dp)
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyRow(
                        modifier = Modifier
                            .background(Jade)
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
