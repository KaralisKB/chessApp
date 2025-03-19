package com.example.chess.local.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.Piece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class BoardState(
    val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) {null } }
){
    val boardStateScope = CoroutineScope(Dispatchers.Default + CoroutineName("BoardStateScope"))
    val killedWhitePieces = mutableListOf<ChessPiece?>()
    val killedBlackPieces = mutableListOf<ChessPiece?>()
    val whiteKing by mutableStateOf(board[0][4])
    val blackKing by mutableStateOf(board[7][4])

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

    }

    fun move(
        piece: ChessPiece,
        to: Position,
        whiteInCheck: Boolean,
        blackInCheck: Boolean
    ) {
        val oldPosition = piece.position
        // Castle Logic
        if (piece.type == PieceType.KING &&
            piece.movesMade == 0 && (!whiteInCheck && !blackInCheck) && (to == Position(
                0,
                6,
                FieldState.VALID
            ) ||
                    to == Position(0, 2, FieldState.VALID) ||
                    to == Position(7, 6, FieldState.VALID) ||
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
                    board[0][7]?.position = Position(0, 5, FieldState.EMPTY)

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
        }
    }

    fun attack(piece: ChessPiece, to: Position) {
        when (board[to.row][to.col] != null) {
            (board[to.row][to.col]?.color == PieceColor.WHITE) -> killedWhitePieces.add(
                board[to.row][to.col]
            )

            (board[to.row][to.col]?.color == PieceColor.BLACK) -> killedBlackPieces.add(
                board[to.row][to.col]
            )

            else -> null
        }

        board[to.row][to.col] = piece
        board[piece.position.row][piece.position.col] = null
        piece.position = to
        piece.movesMade++
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

    fun blockCheck(
        selectedPiece: ChessPiece?,
        attackedKing: ChessPiece,
        proposedBlock: Position,
        state: BoardState
    ): Boolean {
        if (selectedPiece == null) return false

        val oldPosition = selectedPiece.position
        val tempBoard = state.board.map { it.clone() }.toTypedArray()

        tempBoard[proposedBlock.row][proposedBlock.col] = selectedPiece
        tempBoard[oldPosition.row][oldPosition.col] = null
        selectedPiece.position = proposedBlock

        val isBlock = isCheckBlocked(attackedKing, BoardState(tempBoard))

        selectedPiece.position = oldPosition

        return isBlock
    }

    private fun isCheckBlocked(king: ChessPiece, state: BoardState): Boolean {
        val someCheck = checkCheck(king, state)
        return when (king.color) {
            PieceColor.WHITE -> !someCheck.first
            PieceColor.BLACK -> !someCheck.second
        }
    }

    suspend fun checkKingMoveInCheck(king: ChessPiece, boardState: BoardState, to: Position): Boolean {
        val originalPosition = king.position
        val oldPiece = boardState.board[to.row][to.col]

        boardState.board[to.row][to.col] = king
        boardState.board[originalPosition.row][originalPosition.col] = null
        king.position = to

        val enemyMoves = boardStateScope.async(Dispatchers.Default) {
            king.getEnemyMoves(boardState).toList()
        }.await()
        val isStillInCheck = enemyMoves.any { it.row == to.row && it.col == to.col }

        boardState.board[originalPosition.row][originalPosition.col] = king
        boardState.board[to.row][to.col] = oldPiece
        king.position = originalPosition

        return isStillInCheck
    }


    fun isCheckmate(king: ChessPiece, boardState: BoardState): Boolean {
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
                    ?.any { move -> blockCheck(piece, king, move, boardState) } ?: false
            }
        }
        return true
    }
}
