package com.example.chess.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chess.R

@Composable
fun ChessLazyHorizontalGrid(data: List<ChessPiece?>) {
    LazyHorizontalGrid(
        rows = GridCells.Adaptive(minSize = 20.dp),

        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 15.dp)
    ) {
        items(data) { piece ->
            Piece(piece)
        }
    }
}

@Composable
fun Piece(piece: ChessPiece?) {
    if (piece == null) return
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