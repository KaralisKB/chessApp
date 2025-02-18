package com.example.chess.model

class Queen(override val color: PieceColor, startPosition: Position) : ChessPiece {
    override val type: PieceType = PieceType.QUEEN
    override var position: Position = startPosition
    override var isCaptured: Boolean = false
    override var movesMade: Int = 0

    override fun getPossibleMoves(boardState: BoardState): List<Position>? {
        val possibleMoves: MutableList<Position> = mutableListOf()

        val potentialMoves = getPotentialMoves(boardState)

        for (set in 1..8) {
            var sublist: List<Pair<Int, Int>>? = null
            when (set) {
                1 -> sublist = potentialMoves.subList(0, 7)
                2 -> sublist = potentialMoves.subList(8, 15)
                3 -> sublist = potentialMoves.subList(16, 23)
                4 -> sublist = potentialMoves.subList(24, 31)
                5 -> sublist = potentialMoves.subList(32, 39)
                6 -> sublist = potentialMoves.subList(40, 47)
                7 -> sublist = potentialMoves.subList(48, 55)
                8 -> sublist = potentialMoves.subList(56, 63)
            }

            if (sublist!= null) {
                for (move in sublist) {
                    val isValid = isMoveValid(move, boardState)
                    if (isValid == 1) {
                        possibleMoves.add(Position(move.first, move.second, FieldState.VALID))
                    } else if (isValid == 2) {
                        possibleMoves.add(Position(move.first, move.second, FieldState.ATTACK))
                        break
                    } else if (isValid == 0) {
                        break
                    }
                }
            }
        }
        return possibleMoves
    }

    override fun getPotentialMoves(boardState: BoardState): List<Pair<Int, Int>> {
        val potentialMoves = mutableListOf<Pair<Int, Int>>()

        for (direction in 1 ..8) {
            when (direction) {
                1 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col))
                }
                2 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col + square))
                }
                3 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row, position.col + square))
                }
                4 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col + square))
                }
                5 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col))
                }
                6 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row - square, position.col - square))
                }
                7 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row, position.col - square))
                }
                8 -> for(square in 1..8) {
                    potentialMoves.add(Pair(position.row + square, position.col - square))
                }
            }
        }
        return potentialMoves
    }

    override fun isMoveValid(to: Pair<Int, Int>, boardState: BoardState): Int {
        val row = to.first
        val col = to.second

        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return 0
        }

        val targetPiece = boardState.board[row][col]

        if (targetPiece != null) {
            if (targetPiece.color != color) {
                return 2
            }
        } else {
            return 1
        }
        return 0
    }
}