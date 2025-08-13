package com.example.chess.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chess.R
import com.example.chess.local.model.Action
import com.example.chess.local.model.ActionType
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.King
import com.example.chess.local.model.Position
import com.example.chess.ui.board.GameViewModel
import com.example.chess.ui.navigation.Screen
import com.example.chess.ui.theme.LightMain

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ActionList(
    actionList: List<Action?>,
    viewModel: GameViewModel,
    navController: NavController
    ) {
    val listState = rememberLazyListState()

    LaunchedEffect(actionList.size) {
        if (actionList.isNotEmpty()) {
            listState.animateScrollToItem(actionList.lastIndex)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(290.dp)
            .border(
                width = 4.dp,
                color = Color.White,
                shape = RoundedCornerShape(4)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = LightMain)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .border(2.dp, Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatTime(viewModel.blackTimeRemaining!!),
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxSize()
                    .background(LightMain),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.forfeit()
                        navController.navigate(Screen.MainMenu)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightMain),
                    elevation = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text("Resign", color = Color.White)
                }
            }
            Box(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = formatTime(viewModel.whiteTimeRemaining!!),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp).background(Color.White))
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(4.dp)
        ) {
            items(actionList) { action ->
                when (action?.type) {
                    ActionType.MOVE -> MoveActionEntry(action)
                    ActionType.ATTACK -> AttackActionEntry(action)
                    ActionType.CASTLE -> CastleActionEntry(action)
                    ActionType.PROMOTION -> PromotionActionEntry(action)
                    null -> null
                }
            }
        }
    }
}

@Composable
fun MoveActionEntry(action: Action) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = action.turnId.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
        Image(
            painter = painterResource(id = getImageId(piece = action.originalPiece)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        ActionLottie(Modifier.size(40.dp), R.raw.move_icon, 1f)
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.newPosition.col),
                action.newPosition.row
            ),
            fontWeight = FontWeight.Bold
        )
        if(action.colorInCheck != null) {

            val kingInCheck = when (action.colorInCheck) {
                PieceColor.WHITE -> King(PieceColor.WHITE, Position(1, 2, FieldState.EMPTY))
                PieceColor.BLACK -> King(PieceColor.BLACK, Position(1, 2, FieldState.EMPTY))
                null -> TODO()
            }

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = getImageId(kingInCheck)),
                contentDescription = action.originalPiece.type.toString(),
                modifier = Modifier
                    .size(30.dp)
            )
            ActionLottie(Modifier.size(40.dp), R.raw.check_icon, 1f)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = formatTime(action.time)
        )
    }
}

@Composable
fun AttackActionEntry(action: Action) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = action.turnId.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
        Image(
            painter = painterResource(id = getImageId(piece = action.originalPiece)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.originalPosition.col),
                action.originalPosition.row + 1
            ),
            fontWeight = FontWeight.Bold
        )
        ActionLottie(Modifier.size(40.dp), R.raw.attack_icon, 1f)
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.newPosition.col),
                action.newPosition.row + 1
            ),
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = getImageId(piece = action.killedPiece!!)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        if(action.colorInCheck != null) {

            val kingInCheck = when (action.colorInCheck) {
                PieceColor.WHITE -> King(PieceColor.WHITE, Position(1, 2, FieldState.EMPTY))
                PieceColor.BLACK -> King(PieceColor.BLACK, Position(1, 2, FieldState.EMPTY))
                null -> TODO()
            }

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = getImageId(kingInCheck)),
                contentDescription = action.originalPiece.type.toString(),
                modifier = Modifier
                    .size(30.dp)
            )
            ActionLottie(Modifier.size(40.dp), R.raw.check_icon, 1f)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = formatTime(action.time)
        )
    }
}

@Composable
fun CastleActionEntry(action: Action) {
    val arrow = if (action.castleIsLong == true) " <-" else " ->"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = action.turnId.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
        Image(
            painter = painterResource(id = getImageId(piece = action.originalPiece)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.originalPosition.col),
                action.originalPosition.row + 1
            ),
            fontWeight = FontWeight.Bold
        )
        ActionLottie(Modifier.size(40.dp), R.raw.castle_icon, 1f)
        Text(
            text = arrow,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = formatTime(action.time)
        )
    }
}

@Composable
fun PromotionActionEntry(action: Action) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = action.turnId.toString(),
                fontWeight = FontWeight.Bold,
            )
        }
        Image(
            painter = painterResource(id = getImageId(piece = action.originalPiece)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.originalPosition.col),
                action.originalPosition.row + 1
            ),
            fontWeight = FontWeight.Bold
        )
        ActionLottie(Modifier.size(40.dp), R.raw.promotion_icon, 1f)
        Text(
            text = stringResource(
                R.string.move,
                toLetter(action.newPosition.col),
                action.newPosition.row + 1
            ),
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = getImageId(piece = action.promotedToPiece!!)),
            contentDescription = action.promotedToPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = formatTime(action.time)
        )
    }
}


fun getImageId(piece: ChessPiece): Int {
    val res: Int
    if (piece.color == PieceColor.WHITE) {
        res = when (piece.type) {
            PieceType.PAWN -> R.drawable.chess_plt60
            PieceType.KNIGHT -> R.drawable.chess_nlt60
            PieceType.BISHOP -> R.drawable.chess_blt60
            PieceType.ROOK -> R.drawable.chess_rlt60
            PieceType.QUEEN -> R.drawable.chess_qlt60
            PieceType.KING -> R.drawable.chess_klt60
        }
    } else {
        res = when (piece.type) {
            PieceType.PAWN -> R.drawable.chess_pdt60
            PieceType.KNIGHT -> R.drawable.chess_ndt60
            PieceType.BISHOP -> R.drawable.chess_bdt60
            PieceType.ROOK -> R.drawable.chess_rdt60
            PieceType.QUEEN -> R.drawable.chess_qdt60
            PieceType.KING -> R.drawable.chess_kdt60
        }
    }
    return res
}

fun toLetter(column: Int): String {
    val res = when (column) {
        0 -> "A"
        1 -> "B"
        2 -> "C"
        3 -> "D"
        4 -> "E"
        5 -> "F"
        6 -> "G"
        7 -> "H"
        else -> "X"
    }
    return res
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

