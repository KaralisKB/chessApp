package com.example.chess.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chess.R
import com.example.chess.local.model.Action
import com.example.chess.local.model.ActionType
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.King
import com.example.chess.local.model.Position
import com.example.chess.ui.board.GameTimer
import com.example.chess.ui.theme.Jade

@Composable
fun ActionList(actionList: List<Action?>) {
    val listState = rememberLazyListState()

    LaunchedEffect(actionList.size) {
        if (actionList.isNotEmpty()) {
            listState.animateScrollToItem(actionList.lastIndex)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.95f)
            .border(
                width = 4.dp,
                color = Color.White,
                shape = RoundedCornerShape(4)
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Jade)
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                text = GameTimer.getElapsedTime(System.currentTimeMillis())
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(4.dp)
        ) {
            var actionNumber = 1
            items(actionList) { action ->
                when (action?.type) {
                    ActionType.MOVE -> MoveActionEntry(action, actionNumber)
                    ActionType.ATTACK -> AttackActionEntry(action, actionNumber)
                    ActionType.CASTLE -> CastleActionEntry(action, actionNumber)
                    ActionType.PROMOTION -> PromotionActionEntry(action, actionNumber)
                    ActionType.CHECK -> CheckActionEntry(action, actionNumber)
                    null -> null
                }
                actionNumber++
            }
        }
    }
}

// todo( CONDENSE

@Composable
fun MoveActionEntry(action: Action, actionNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            Text(
                text = actionNumber.toString(),
                fontWeight = FontWeight.Bold
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
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(4.dp),
            text = GameTimer.getElapsedTime(action.time)
        )
    }
}

@Composable
fun AttackActionEntry(action: Action, actionNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.padding(4.dp)) {
            Text(
                text = actionNumber.toString(),
                fontWeight = FontWeight.Bold
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
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(4.dp),
            text = GameTimer.getElapsedTime(action.time)
        )
    }
}

@Composable
fun CastleActionEntry(action: Action, actionNumber: Int) {
    val arrow = if (action.castleIsLong == true) " <-" else " ->"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionNumber.toString(),
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = getImageId(piece = action.originalPiece)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        ActionLottie(Modifier.size(40.dp), R.raw.castle_icon, 1f)
        Text(
            text = arrow,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(4.dp),
            text = GameTimer.getElapsedTime(action.time)
        )
    }
}

@Composable
fun PromotionActionEntry(action: Action, actionNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionNumber.toString(),
            fontWeight = FontWeight.Bold
        )
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
            modifier = Modifier.padding(4.dp),
            text = GameTimer.getElapsedTime(action.time)
        )
    }
}

@Composable
fun CheckActionEntry(action: Action, actionNumber: Int) {
    val kingInCheck = if (action.whiteInCheck == true) King(
        PieceColor.WHITE,
        Position(1, 2, FieldState.EMPTY)
    ) else King(PieceColor.BLACK, Position(1, 2, FieldState.EMPTY))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = actionNumber.toString(),
            fontWeight = FontWeight.Bold
        )
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
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = getImageId(kingInCheck)),
            contentDescription = action.originalPiece.type.toString(),
            modifier = Modifier
                .size(30.dp)
        )
        ActionLottie(Modifier.size(40.dp), R.raw.check_icon, 1f)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.padding(4.dp),
            text = GameTimer.getElapsedTime(action.time)
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

