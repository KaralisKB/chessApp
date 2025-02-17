package com.example.chess.model

class BoardState {
    val board = Array(8) { Array<ChessPiece?>(8) {null } }

    init {
        board[0][0] = Rook(PieceColor.WHITE, Position(0, 0)) // A1
        board[0][1] = Knight(PieceColor.WHITE, Position(0, 1)) // B1
        board[0][2] = Bishop(PieceColor.WHITE, Position(0, 2))
        board[0][3] = Queen(PieceColor.WHITE, Position(0, 3))
        board[0][4] = King(PieceColor.WHITE, Position(0, 4))
        board[0][5] = Bishop(PieceColor.WHITE, Position(0, 5))
        board[0][6] = Knight(PieceColor.WHITE, Position(0, 6))
        board[5][5] = Rook(PieceColor.WHITE, Position(5, 5))

        for (i in 0..7) {
            board[1][i] = Pawn(PieceColor.WHITE, Position(1, i))
        }

        board[7][0] = Rook(PieceColor.BLACK, Position(7, 0)) // A1
        board[7][1] = Knight(PieceColor.BLACK, Position(7, 1)) // B1
        board[7][2] = Bishop(PieceColor.BLACK, Position(7, 2))
        board[7][3] = Queen(PieceColor.BLACK, Position(7, 3))
        board[7][4] = King(PieceColor.BLACK, Position(7, 4))
        board[3][1] = Bishop(PieceColor.BLACK, Position(3, 1))
        board[4][3] = Knight(PieceColor.BLACK, Position(4, 3))
        board[5][2] = Rook(PieceColor.BLACK, Position(5, 2))

        for (i in 0..7) {
            board[6][i] = Pawn(PieceColor.BLACK, Position(6, i))
        }

    }
}
