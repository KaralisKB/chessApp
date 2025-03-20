package com.example.chess.ui.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.chess.domain.useCase.board.MovePieceUseCase
import com.example.chess.local.model.BoardState
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.Position
import com.example.chess.ui.base.BaseViewModel
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.utils.ext.isWhite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor (
    private val movePieceUseCase: MovePieceUseCase
) : BaseViewModel(Dispatchers.Default) {
    var selectedPiece by mutableStateOf<ChessPiece?>(null)
    private var isWhiteTurn by mutableStateOf(true)
    var possibleMoves by mutableStateOf<List<Position>>(listOf())
    var board by mutableStateOf(BoardState())
    var clickedSquare by mutableStateOf<Position?>(null)
    private var whiteInCheck by mutableStateOf(false)
    private var blackInCheck by mutableStateOf(false)
    private val whiteKing by mutableStateOf(board.board[0][4])
    private val blackKing by mutableStateOf(board.board[7][4])
    private var attackingPiece by mutableStateOf<ChessPiece?>(null)
    val onPromotionGranted: () -> Unit = {
        selectPiece(null)
        possibleMoves = listOf()
        checkCheckCheck(null)
        changeTurn()
    }

    private var boardArray = board.board
    private val someBullShit = MutableLiveData<String>()

    private fun selectPiece(selectedPiece: ChessPiece?) {
        this.selectedPiece = when (selectedPiece == null) {
            true -> null
            false -> if ((selectedPiece.color == PieceColor.WHITE) == isWhiteTurn) selectedPiece else null
        }
    }

    private fun changeTurn() {
        isWhiteTurn = !isWhiteTurn
        selectedPiece = null
        possibleMoves = listOf()
        println("Turn changed: White's Turn = $isWhiteTurn")

    }

    private fun checkCheckCheck(attackingPiece: ChessPiece?) {
        if (board.checkCheck(whiteKing!!, board).first) {
            whiteInCheck = true
            this.attackingPiece = attackingPiece
            if (board.isCheckmate(whiteKing!!, board)) {
                println("Black Wins!")
            }
        } else if (board.checkCheck(blackKing!!, board).second) {
            blackInCheck = true
            if (board.isCheckmate(blackKing!!, board)) {
                print("White Wins!")
            }
        } else {
            whiteInCheck = false
            blackInCheck = false
            this.attackingPiece = null
        }
    }

    private fun _isPromotionPossible(piece: ChessPiece?, clickedSquare: Position?): Boolean {
        return when {
            piece == null || clickedSquare == null -> false
            piece.type == PieceType.PAWN && piece.movesMade >= 4 && (clickedSquare.row == 0 || clickedSquare.row == 7) ->
                piece.isWhite() && piece.position.row == 6 ||
                        !piece.isWhite() && piece.position.row == 1
             else -> false
        }
    }

    val isPromotionPossible: Boolean
        get() = _isPromotionPossible(selectedPiece, clickedSquare)

    fun handleSquareClick(row: Int, col: Int) {
        clickedSquare = Position(row, col, FieldState.EMPTY)
        val clickedPiece = this.boardArray[row][col]

        if (clickedPiece == selectedPiece) {
            selectedPiece = clickedPiece
            return
        }

        if (!_isPromotionPossible(selectedPiece, clickedSquare)) {
            when {
                (selectedPiece != null && clickedPiece == null && possibleMoves.contains(Position(row, col, FieldState.VALID))) -> {
                    board.move(
                        selectedPiece!!,
                        Position(row, col, FieldState.VALID),
                        whiteInCheck,
                        blackInCheck
                    )
                    viewModelScope.launch {
                        possibleMoves = withContext(Dispatchers.Default) {
                            selectedPiece?.getPossibleMoves(board, null) ?: listOf()
                        }
                    }
                    changeTurn()

                    checkCheckCheck(selectedPiece)
                }

                (selectedPiece != null && clickedPiece != null && possibleMoves.contains(Position(row, col, FieldState.ATTACK))) -> {
                    board.attack(selectedPiece!!, Position(row, col, FieldState.ATTACK))
                    viewModelScope.launch {
                        possibleMoves = withContext(Dispatchers.Default) {
                            selectedPiece?.getPossibleMoves(board, null) ?: listOf()
                        }
                    }
                    changeTurn()
                    checkCheckCheck(selectedPiece)
                }

                else -> {
                    selectPiece(clickedPiece)
                    val updatedSelectedPiece =
                        if (clickedPiece?.color == selectedPiece?.color) clickedPiece else selectedPiece

                    val params = MovePieceUseCase.Params.create(
                        piece = updatedSelectedPiece,
                        board = board,
                        whiteInCheck,
                        blackInCheck
                    )
                    ioToUi(
                        io = { movePieceUseCase.execute(params) },
                        ui = { possibleMoves = checkMoveParser(whiteInCheck, blackInCheck, updatedSelectedPiece?.getPossibleMoves(board) ?: emptyList() )}
                    )
                }
            }
        }
    }

    private fun checkMoveParser(
        whiteInCheck: Boolean,
        blackInCheck: Boolean,
        possibleMoves: List<Position>
    ): List<Position> {
        println("Before filtering: ${possibleMoves.size} moves available for ${selectedPiece?.type}")

        val filteredMoves = when {
            whiteInCheck && selectedPiece != whiteKing -> {
                possibleMoves.filter { board.blockCheck(selectedPiece, whiteKing!!, it, board) }
            }
            blackInCheck && selectedPiece != blackKing -> {
                possibleMoves.filter { board.blockCheck(selectedPiece, blackKing!!, it, board) }
            }
            else -> possibleMoves.filter { board.xrayCheck(selectedPiece, it, board) }
        }

        println("After filtering: ${filteredMoves.size} moves remain for ${selectedPiece?.type}")
        return filteredMoves
    }

}