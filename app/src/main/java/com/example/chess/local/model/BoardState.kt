package com.example.chess.local.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Suppress("UNUSED_EXPRESSION")
data class BoardState(
    val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) { null } }
) {
    private val boardStateScope =
        CoroutineScope(Dispatchers.Default + CoroutineName("BoardStateScope"))
    val killedWhitePieces = mutableListOf<ChessPiece?>()
    val killedBlackPieces = mutableListOf<ChessPiece?>()
    var whiteKing by mutableStateOf<ChessPiece?>(null)
    var blackKing by mutableStateOf<ChessPiece?>(null)

    init {
        board[0][0] = Rook(PieceColor.WHITE, Position(0, 0, FieldState.EMPTY)) // A1
        board[0][1] = Knight(PieceColor.WHITE, Position(0, 1, FieldState.EMPTY)) // B1
        board[0][2] = Bishop(PieceColor.WHITE, Position(0, 2, FieldState.EMPTY))
        board[0][3] = Queen(PieceColor.WHITE, Position(0, 3, FieldState.EMPTY))
        board[0][4] = King(PieceColor.WHITE, Position(0, 4, FieldState.EMPTY))
        board[0][5] = Bishop(PieceColor.WHITE, Position(0, 5, FieldState.EMPTY))
        board[0][6] = Knight(PieceColor.WHITE, Position(0, 6, FieldState.EMPTY))
        board[0][7] = Rook(PieceColor.WHITE, Position(0, 7, FieldState.EMPTY))

        for (i in 0..7) {
            board[1][i] = Pawn(PieceColor.WHITE, Position(1, i, FieldState.EMPTY))
        }

        board[7][0] = Rook(PieceColor.BLACK, Position(7, 0, FieldState.EMPTY)) // A1
        board[7][1] = Knight(PieceColor.BLACK, Position(7, 1, FieldState.EMPTY)) // B1
        board[7][2] = Bishop(PieceColor.BLACK, Position(7, 2, FieldState.EMPTY))
        board[7][3] = Queen(PieceColor.BLACK, Position(7, 3, FieldState.EMPTY))
        board[7][4] = King(PieceColor.BLACK, Position(7, 4, FieldState.EMPTY))
        board[7][5] = Bishop(PieceColor.BLACK, Position(7, 5, FieldState.EMPTY))
        board[7][6] = Knight(PieceColor.BLACK, Position(7, 6, FieldState.EMPTY))
        board[7][7] = Rook(PieceColor.BLACK, Position(7, 7, FieldState.EMPTY))

        for (i in 0..7) {
            board[6][i] = Pawn(PieceColor.BLACK, Position(6, i, FieldState.EMPTY))
        }

        whiteKing = board[0][4]
        blackKing = board[7][4]
    }

    fun move(
        piece: ChessPiece,
        to: Position,
        whiteInCheck: Boolean,
        blackInCheck: Boolean
    ): Boolean {
        val oldPosition = piece.position

        var isCastle = true

        if (piece.type == PieceType.KING &&
            piece.movesMade == 0 &&
            (!whiteInCheck && !blackInCheck) &&
            (to == Position(0,6,FieldState.VALID)||
             to == Position(0, 2, FieldState.VALID)||
             to == Position(7, 6, FieldState.VALID)||
             to == Position(7, 2, FieldState.VALID))
        ) {
            when {
                (piece.color == PieceColor.WHITE) -> if (to == Position(
                        0,
                        6,
                        FieldState.VALID
                    ) && board[0][7]?.movesMade == 0
                ) {
                    //king move
                    board[0][6] = piece
                    piece.position = to
                    board[0][4] = null

                    //rook move
                    board[0][5] = board[0][7]
                    board[0][7] = null
                    board[0][5]?.position = Position(0, 5, FieldState.EMPTY)

                    // moves made adjustment
                    piece.movesMade++
                    board[0][7]?.movesMade = board[0][7]?.movesMade!! + 1


                } else if (to == Position(
                        0,
                        2,
                        FieldState.VALID
                    ) && board[0][0]?.movesMade == 0
                ) {
                    //king move
                    board[0][2] = piece
                    piece.position = to
                    board[0][4] = null

                    //rook move
                    board[0][3] = board[0][0]
                    board[0][0] = null
                    board[0][3]?.position = Position(0, 3, FieldState.EMPTY)


                    // moves made adjustment
                    piece.movesMade++
                    board[0][3]?.movesMade = board[0][3]?.movesMade!! + 1
                }

                (piece.color == PieceColor.BLACK) -> if (to == Position(
                        7,
                        6,
                        FieldState.VALID
                    ) && board[7][7]?.movesMade == 0
                ) {
                    //king move
                    board[7][6] = piece
                    piece.position = to
                    board[7][4] = null

                    //rook move
                    board[7][5] = board[7][7]
                    board[7][7] = null
                    board[7][5]?.position = Position(7, 5, FieldState.EMPTY)

                    // moves made adjustment
                    piece.movesMade++
                    board[7][7]?.movesMade = board[7][7]?.movesMade!! + 1
                } else if (to == Position(
                        7,
                        2,
                        FieldState.VALID
                    ) && board[7][0]?.movesMade == 0
                ) {
                    //king move
                    board[7][2] = piece
                    piece.position = to
                    board[7][4] = null

                    //rook move
                    board[7][3] = board[7][0]
                    board[7][0] = null
                    board[7][3]?.position = Position(7, 3, FieldState.EMPTY)

                    // moves made adjustment
                    piece.movesMade++
                    board[7][3]?.movesMade = board[7][3]?.movesMade!! + 1
                }
            }
        } else {
            board[to.row][to.col] = piece
            board[oldPosition.row][oldPosition.col] = null
            piece.position = to
            piece.movesMade++
            isCastle = false
        }
        if (piece.type == PieceType.KING) {
            if (piece.color == PieceColor.WHITE) whiteKing = piece else blackKing = piece
        }
        return isCastle
    }

    fun attack(piece: ChessPiece, to: Position) {
        when (board[to.row][to.col] != null) {

            (board[to.row][to.col]?.color == PieceColor.WHITE) ->
                killedWhitePieces.add(board[to.row][to.col])

            (board[to.row][to.col]?.color == PieceColor.BLACK) ->
                killedBlackPieces.add(board[to.row][to.col])

            else -> null
        }

        board[to.row][to.col] = piece
        board[piece.position.row][piece.position.col] = null
        piece.position = to
        piece.movesMade++

        if (piece.type == PieceType.KING) {
            if (piece.color == PieceColor.WHITE) whiteKing = piece else blackKing = piece
        }
    }

    fun checkCheck(king: ChessPiece, state: BoardState): Pair<Boolean, Boolean> {
        var whiteInCheck = false
        var blackInCheck = false

        val attackingMoves = runBlocking {
            king.getEnemyMoves(state).toList()
        }

        if (attackingMoves.any { it.row == king.position.row && it.col == king.position.col }) {
            king.inCheck = true
            if (king.color == PieceColor.BLACK) blackInCheck = true
            else whiteInCheck = true
        } else {
            king.inCheck = false
        }

        return Pair(whiteInCheck, blackInCheck)
    }

    fun blockCheck(proposedBlock: Position, lineOfAttack: List<Position>): Boolean {
        val isBlock = if (proposedBlock in lineOfAttack) true else false
        return isBlock
    }

    fun xrayCheck(selectedPiece: ChessPiece?, proposedMove: Position, state: BoardState): Boolean {

        if (selectedPiece == null) return false

        val oldPosition = selectedPiece.position
        val tempBoard = BoardState(
            board = state.board.map { it.clone() }.toTypedArray()
        )

        tempBoard.board[proposedMove.row][proposedMove.col] = selectedPiece
        tempBoard.board[oldPosition.row][oldPosition.col] = null
        selectedPiece.position = proposedMove

        if (selectedPiece.type == PieceType.KING && selectedPiece.color == PieceColor.WHITE) {
            tempBoard.whiteKing = selectedPiece
        } else { tempBoard.blackKing = selectedPiece }

        if (selectedPiece.type == PieceType.KING) {
            val isKingSafe = when (selectedPiece.color) {
                PieceColor.WHITE -> {
                    !tempBoard.checkCheck(tempBoard.whiteKing ?: return false, tempBoard).first
                }
                PieceColor.BLACK -> {
                    !tempBoard.checkCheck(tempBoard.blackKing ?: return false, tempBoard).second
                }
            }

            selectedPiece.position = oldPosition

            val targetSquare = state.board[proposedMove.row][proposedMove.col]
            if (targetSquare != null && targetSquare.color == selectedPiece.color) {
                return false
            }
            if (!isKingSafe) {
                proposedMove.type = FieldState.BLOCKED
            }
            return true
        }
        selectedPiece.position = oldPosition
        return true
    }

    fun isLegalMove(piece: ChessPiece, to: Position): Boolean {
        val from = piece.position
        val targetPiece = board[to.row][to.col]
        val originalMovesMade = piece.movesMade

        board[to.row][to.col] = piece
        board[from.row][from.col] = null
        piece.position = to
        piece.movesMade++

        val prevWhiteKing = whiteKing
        val prevBlackKing = blackKing
        if (piece.type == PieceType.KING) {
            if (piece.color == PieceColor.WHITE) whiteKing = piece else blackKing = piece
        }

        val king = if (piece.color == PieceColor.WHITE) whiteKing else blackKing
        val inCheck = checkCheck(king ?: return false, this).let {
            if (piece.color == PieceColor.WHITE) it.first else it.second
        }

        piece.position = from
        piece.movesMade = originalMovesMade
        board[from.row][from.col] = piece
        board[to.row][to.col] = targetPiece

        whiteKing = prevWhiteKing
        blackKing = prevBlackKing

        return !inCheck
    }

    fun isCheckmate(king: ChessPiece, boardState: BoardState, lineOfAttack: List<Position>): Boolean {
        if (!king.inCheck) return false

        var possibleMoves: List<Position>? = null
        boardStateScope.launch { possibleMoves = king.getPossibleMoves(boardState, null) }
        if (possibleMoves != null)
            if (possibleMoves!!.any { it.type == FieldState.VALID || it.type == FieldState.ATTACK }) return false

        val allPieces = boardState.board.flatten().filterNotNull()
            .filter { it.color == king.color && it.type != PieceType.KING }
        boardStateScope.launch(Dispatchers.Default) {
            allPieces.any { piece ->
                piece
                    .getPossibleMoves(boardState, null)
                    ?.any { move -> blockCheck( move, lineOfAttack) } ?: false
            }
        }
        return true
    }
}
