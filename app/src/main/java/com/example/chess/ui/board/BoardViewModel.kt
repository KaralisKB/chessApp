package com.example.chess.ui.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.chess.local.model.BoardState
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.Position
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType

class BoardViewModel : ViewModel() {
    var selectedPiece by mutableStateOf<ChessPiece?>(null)
    var isWhiteTurn by mutableStateOf(true)
    var possibleMoves by mutableStateOf<List<Position>?>(null)
    var board by mutableStateOf(BoardState())
    var isPromotionPossible by mutableStateOf(false)
    var clickedSquare by mutableStateOf<Position?>(null)
    var whiteInCheck by mutableStateOf(false)
    var blackInCheck by mutableStateOf(false)
    val whiteKing by mutableStateOf(board.board[0][4])
    val blackKing by mutableStateOf(board.board[7][4])
    val onPromotionGranted: () -> Unit = {
        selectPiece(null)
        possibleMoves = null
        isPromotionPossible = false
        checkCheckCheck()
    }

    private fun selectPiece(selectedPiece: ChessPiece?) {
        this.selectedPiece = when (selectedPiece == null) {
            true -> null
            false -> if ((selectedPiece.color == PieceColor.WHITE) == isWhiteTurn) selectedPiece else null
        }
    }

    private fun changeTurn() {
        isWhiteTurn = !isWhiteTurn
        selectedPiece = null
        possibleMoves = null
    }

    private fun checkCheckCheck(){
        if(board.checkCheck(whiteKing!!, whiteKing!!.getEnemyMoves(board).toList()).first) {
            whiteInCheck = true
            if(board.isCheckmate(whiteKing!!, board)) {
                println("Black Wins!")
            }
        } else if(board.checkCheck(blackKing!!, blackKing!!.getEnemyMoves(board).toList()).second) {
            blackInCheck = true
            if(board.isCheckmate(blackKing!!, board)){
                print("White Wins!")
            }
        } else {
            whiteInCheck = false
            blackInCheck = false
        }
    }

    private fun promotionCheck(selectedPiece: ChessPiece?, clickedSquare: Position) {
        if (selectedPiece?.type == PieceType.PAWN && selectedPiece.movesMade >= 4 && (clickedSquare.row == 0 || clickedSquare.row == 7)) {
            if (selectedPiece.color == PieceColor.WHITE && selectedPiece.position.row == 6) {
                isPromotionPossible = true
            } else if (selectedPiece.color == PieceColor.BLACK && selectedPiece.position.row == 1) {
                isPromotionPossible = true
            }
        }
    }

    private fun blockCheckCheck() {
        if ((whiteInCheck || blackInCheck) && selectedPiece?.type != PieceType.KING) {
            when {
                (whiteInCheck && selectedPiece?.color == PieceColor.WHITE) -> possibleMoves =
                    possibleMoves?.filter { board.blockCheck(selectedPiece!!, whiteKing!!, it, board)}
                (blackInCheck && selectedPiece?.color == PieceColor.BLACK) -> possibleMoves =
                    possibleMoves?.filter { board.blockCheck(selectedPiece!!, blackKing!!, it, board) }
            }
        } else if ((whiteInCheck || blackInCheck) && selectedPiece?.type == PieceType.KING) {
            possibleMoves = selectedPiece?.getPossibleMoves(board, null)
        }
    }

    fun handleSquareClick(row: Int, col: Int) {
        clickedSquare = Position(row, col, FieldState.EMPTY)
        val clickedPiece = this.board.board[row][col]
        promotionCheck(selectedPiece, clickedSquare!!)

        if (!isPromotionPossible) {
            when {
                (selectedPiece != null && clickedPiece == null &&
                        possibleMoves?.contains(Position(row, col, FieldState.VALID)) == true) -> {
                    board.move(selectedPiece!!, Position(row, col, FieldState.VALID), whiteInCheck, blackInCheck)
                    possibleMoves = selectedPiece?.getPossibleMoves(board, null)
                    changeTurn()
                    checkCheckCheck()
                    isPromotionPossible = false
                }

                (selectedPiece != null && clickedPiece != null &&
                        possibleMoves?.contains(Position(row, col, FieldState.ATTACK)) == true) -> {
                    board.attack(selectedPiece!!, Position(row, col, FieldState.ATTACK))
                    possibleMoves = selectedPiece?.getPossibleMoves(board, null)
                    changeTurn()
                    checkCheckCheck()
                    isPromotionPossible = false
                }

                else -> {
                    selectPiece(clickedPiece)
                    val updatedSelectedPiece =
                        if (clickedPiece?.color == selectedPiece?.color) clickedPiece else null
                    possibleMoves = updatedSelectedPiece?.getPossibleMoves(board, null)
                    blockCheckCheck()
                    isPromotionPossible = false
                }
            }
        }
    }
}