package com.example.chess.model

import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType

data class BoardState(
    val board: Array<Array<ChessPiece?>> = Array(8) { Array(8) {null } }
){
    val killedWhitePieces = mutableListOf<ChessPiece?>()
    val killedBlackPieces = mutableListOf<ChessPiece?>()

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

    fun move(piece: ChessPiece, to: Position, whiteInCheck: Boolean, blackInCheck: Boolean) {
        // Castle Logic
        if(piece.type == PieceType.KING &&
                piece.movesMade == 0 && (!whiteInCheck && !blackInCheck) && (to == Position(0, 6, FieldState.VALID) ||
                to == Position(0, 2, FieldState.VALID) ||
                to == Position(7, 6, FieldState.VALID) ||
                to == Position(7, 2, FieldState.VALID))
        ) {
            when {
                (piece.color == PieceColor.WHITE) -> if (to == Position(0, 6, FieldState.VALID) && board[0][7]?.movesMade == 0) {
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
                } else if (to == Position(0, 2, FieldState.VALID) && board[0][0]?.movesMade == 0) {
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

                (piece.color == PieceColor.BLACK) -> if (to == Position(7, 6, FieldState.VALID) && board[7][7]?.movesMade == 0) {
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
                } else if (to == Position(7, 2, FieldState.VALID) && board[7][0]?.movesMade == 0) {
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

                else -> null
            }
        } else {
            //Basic Movement Logic
            board[to.row][to.col] = piece
            board[piece.position.row][piece.position.col] = null
            piece.position = to
            piece.movesMade++
        }
    }

    fun attack(piece: ChessPiece, to: Position) {
        when (board[to.row][to.col] != null) {
            (board[to.row][to.col]?.color == PieceColor.WHITE) -> killedWhitePieces.add(board[to.row][to.col])
            (board[to.row][to.col]?.color == PieceColor.BLACK) -> killedBlackPieces.add(board[to.row][to.col])
            else -> null
        }

        board[to.row][to.col] = piece
        board[piece.position.row][piece.position.col] = null
        piece.position = to
        piece.movesMade++
    }



    fun checkCheck(king: ChessPiece, attackingMoves: List<Position>?): Pair<Boolean, Boolean> {
        var whiteInCheck = false
        var blackInCheck = false
        if (attackingMoves != null &&
            attackingMoves.filter{( 7 >= it.row) && ( it.row >= 0) && (7 >= it.col) && (it.col >= 0)}.any { it.row == king.position.row && it.col == king.position.col}) {
            if (king.color == PieceColor.BLACK) {
                blackInCheck = true
                king.inCheck = true
            } else {
                whiteInCheck = true
                king.inCheck = true
            }
        } else {
            king.inCheck = false
        }

        return Pair(whiteInCheck, blackInCheck)
    }

    fun blockCheck(selectedPiece: ChessPiece, attackedKing: ChessPiece, proposedBlock: Position, attackingMoves: List<Position>?, state: BoardState): Boolean {
        var isBlock = false
        if(board[proposedBlock.row][proposedBlock.col] != null) {
            val killedPiece = board[proposedBlock.row][proposedBlock.col]
            val oldPosition = selectedPiece.position

            board[proposedBlock.row][proposedBlock.col] = selectedPiece
            selectedPiece.position = Position(proposedBlock.row, proposedBlock.col, FieldState.EMPTY)

            when (attackedKing.color) {
                PieceColor.WHITE -> if (state.checkCheck(attackedKing, attackedKing.getEnemyMoves(state).toList()).first) isBlock = false else isBlock = true
                PieceColor.BLACK -> if (state.checkCheck(attackedKing, attackedKing.getEnemyMoves(state).toList()).second) isBlock = false else isBlock = true
            }

            board[oldPosition.row][oldPosition.col] = selectedPiece
            selectedPiece.position = Position(oldPosition.row, oldPosition.col, FieldState.EMPTY)
            board[proposedBlock.row][proposedBlock.col] = killedPiece
        } else {

            val oldPosition = selectedPiece.position

            board[proposedBlock.row][proposedBlock.col] = selectedPiece
            selectedPiece.position = Position(proposedBlock.row, proposedBlock.col, FieldState.EMPTY)

            when (attackedKing.color) {
                PieceColor.WHITE -> if (state.checkCheck(attackedKing, attackedKing.getEnemyMoves(state).toList()).first) isBlock = false else isBlock = true
                PieceColor.BLACK -> if (state.checkCheck(attackedKing, attackedKing.getEnemyMoves(state).toList()).second) isBlock = false else isBlock = true
            }

            board[oldPosition.row][oldPosition.col] = selectedPiece
            board[proposedBlock.row][proposedBlock.col] = null
            selectedPiece.position = Position(oldPosition.row, oldPosition.col, FieldState.EMPTY)
        }
    return isBlock
}

    //TODO fix ts
    fun checkKingMoveInCheck(king: ChessPiece, boardState: BoardState, to: Position): Boolean {
        val oldPosition = king.position
        val oldPiece = boardState.board[to.row][to.col]
        val inCheck: Boolean

        boardState.board[to.row][to.col] = king
        king.position = Position(to.row, to.col, FieldState.VALID)
        boardState.board[oldPosition.row][oldPosition.col] = null

        val enemyMoves = king.getEnemyMoves(boardState)
        inCheck = enemyMoves.any { it.row == to.row && it.col == to.col }

        boardState.board[oldPosition.row][oldPosition.col] = king
        boardState.board[to.row][to.col] = oldPiece
        king.position = oldPosition

        return inCheck
    }

    fun isCheckmate(king: ChessPiece, boardState: BoardState): Boolean {
        if (!king.inCheck) return false

        val possibleMoves = king.getPossibleMoves(boardState, null)
        if (possibleMoves != null) {
            if (possibleMoves.any { it.type == FieldState.VALID || it.type == FieldState.ATTACK }) return false
        }

        val allPieces = boardState.board.flatten().filterNotNull().filter { it.color == king.color && it.type != PieceType.KING }
        for (piece in allPieces) {
            val moves = piece.getPossibleMoves(boardState, null)
            if (moves != null) {
                for (move in moves) {
                    if (boardState.blockCheck(piece, king, move, king.getEnemyMoves(boardState).toList(), boardState)) {
                        return false
                    }
                }
            }
        }

        return true
    }
}
