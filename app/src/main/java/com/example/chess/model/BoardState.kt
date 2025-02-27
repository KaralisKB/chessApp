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
        board[0][4] = King(PieceColor.WHITE, Position(0, 4, FieldState.EMPTY))
        board[5][4] = Bishop(PieceColor.WHITE, Position(5, 4, FieldState.EMPTY))
        board[5][3] = Knight(PieceColor.WHITE, Position(5, 3, FieldState.EMPTY))
        board[0][7] = Rook(PieceColor.WHITE, Position(0, 7, FieldState.EMPTY))

        for (i in 0..7) {
            board[1][i] = Pawn(PieceColor.WHITE, Position(1, i, FieldState.EMPTY))
        }


        board[7][0] = Rook(PieceColor.BLACK, Position(7, 0, FieldState.EMPTY)) // A1
        board[7][1] = Knight(PieceColor.BLACK, Position(7, 1, FieldState.EMPTY)) // B1
        board[7][2] = Bishop(PieceColor.BLACK, Position(7, 2, FieldState.EMPTY))
        board[3][2] = Queen(PieceColor.BLACK, Position(3, 2, FieldState.EMPTY))
        board[7][4] = King(PieceColor.BLACK, Position(7, 4, FieldState.EMPTY))
        board[3][1] = Bishop(PieceColor.BLACK, Position(3, 1, FieldState.EMPTY))
        board[4][3] = Knight(PieceColor.BLACK, Position(4, 3, FieldState.EMPTY))
        board[7][7] = Rook(PieceColor.BLACK, Position(7, 7, FieldState.EMPTY))

        for (i in 0..7) {
            board[6][i] = Pawn(PieceColor.BLACK, Position(6, i, FieldState.EMPTY))
        }

    }

    fun move(piece: ChessPiece, to: Position) {
        // Castle Logic
        if(piece.type == PieceType.KING && piece.movesMade == 0) {
            when {
                (piece.color == PieceColor.WHITE) -> if (to == Position(0, 6, FieldState.VALID) && board[0][7]?.movesMade == 0) {
                    //king move
                    board[0][6] = piece
                    piece.position = Position(to.row, to.col, FieldState.EMPTY)
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
}
