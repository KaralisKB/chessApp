package com.example.chess.model

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
        board[5][3] = King(PieceColor.WHITE, Position(5, 3, FieldState.EMPTY))
        board[0][5] = Bishop(PieceColor.WHITE, Position(0, 5, FieldState.EMPTY))
        board[0][6] = Knight(PieceColor.WHITE, Position(0, 6, FieldState.EMPTY))
        board[5][5] = Rook(PieceColor.WHITE, Position(5, 5, FieldState.EMPTY))

        for (i in 0..7) {
            board[1][i] = Pawn(PieceColor.WHITE, Position(1, i, FieldState.EMPTY))
        }


        board[7][0] = Rook(PieceColor.BLACK, Position(7, 0, FieldState.EMPTY)) // A1
        board[7][1] = Knight(PieceColor.BLACK, Position(7, 1, FieldState.EMPTY)) // B1
        board[7][2] = Bishop(PieceColor.BLACK, Position(7, 2, FieldState.EMPTY))
        board[3][2] = Queen(PieceColor.BLACK, Position(3, 2, FieldState.EMPTY))
        board[2][4] = King(PieceColor.BLACK, Position(2, 4, FieldState.EMPTY))
        board[3][1] = Bishop(PieceColor.BLACK, Position(3, 1, FieldState.EMPTY))
        board[4][3] = Knight(PieceColor.BLACK, Position(4, 3, FieldState.EMPTY))
        board[5][2] = Rook(PieceColor.BLACK, Position(5, 2, FieldState.EMPTY))

        for (i in 0..7) {
            board[6][i] = Pawn(PieceColor.BLACK, Position(6, i, FieldState.EMPTY))
        }

    }

    fun move(piece: ChessPiece, to: Position) {
        board[to.row][to.col] = piece
        board[piece.position.row][piece.position.col] = null
        piece.position = to
        piece.movesMade++
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

    private fun changePosition() {

    }
}
