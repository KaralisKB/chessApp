package com.example.chess.ui.board

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.chess.data.db.entity.GameEntity
import com.example.chess.domain.useCase.board.MovePieceUseCase
import com.example.chess.domain.useCase.game.CreateGameUseCase
import com.example.chess.domain.useCase.game.EndGameUseCase
import com.example.chess.domain.useCase.logging.ClearActionsUseCase
import com.example.chess.domain.useCase.logging.GetActionsUseCase
import com.example.chess.domain.useCase.logging.SaveActionUseCase
import com.example.chess.local.model.Action
import com.example.chess.local.model.ActionType
import com.example.chess.local.model.BoardState
import com.example.chess.local.model.FieldState
import com.example.chess.local.model.GameType
import com.example.chess.local.model.Position
import com.example.chess.ui.base.BaseViewModel
import com.example.chess.ui.components.ChessPiece
import com.example.chess.ui.components.PieceColor
import com.example.chess.ui.components.PieceType
import com.example.chess.ui.navigation.Screen
import com.example.chess.utils.ext.isWhite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val movePieceUseCase: MovePieceUseCase,
    private val saveActionUseCase: SaveActionUseCase,
    private val getActionsUseCase: GetActionsUseCase,
    private val clearActionsUseCase: ClearActionsUseCase,
    private val createGameUseCase: CreateGameUseCase,
    private val endGameUseCase: EndGameUseCase
) : BaseViewModel(Dispatchers.Default) {

    val actionList: StateFlow<List<Action>> =
        getActionsUseCase.execute().toStateFlow(initial = listOf())

    var whiteName by mutableStateOf<String?>(null)
    var blackName by mutableStateOf<String?>(null)
    var gameType by mutableStateOf<GameType?>(null)
    var gameId by mutableStateOf<Long?>(null)

    var navController by mutableStateOf<NavController?>(null)

    private var winner by mutableStateOf<String?>(null)
    var whiteTimeRemaining by mutableStateOf<Long?>(0L)
    var blackTimeRemaining by mutableStateOf<Long?>(0L)

    var selectedPiece by mutableStateOf<ChessPiece?>(null)
    private var isWhiteTurn by mutableStateOf(true)
    var possibleMoves by mutableStateOf<List<Position>>(listOf())
    var board by mutableStateOf(BoardState())
    var clickedSquare by mutableStateOf<Position?>(null)
    var lineOfAttack by mutableStateOf<List<Position>>(listOf())
    private var lastMovedPiece by mutableStateOf<ChessPiece?>(null)
    private var whiteInCheck by mutableStateOf(false)
    private var blackInCheck by mutableStateOf(false)
    private val whiteKing: ChessPiece?
        get() = board.board.flatten().firstOrNull { it?.type == PieceType.KING && it.color == PieceColor.WHITE }

    private val blackKing: ChessPiece?
        get() = board.board.flatten().firstOrNull { it?.type == PieceType.KING && it.color == PieceColor.BLACK }
    private var boardArray = board.board

    val onPromotionGranted: () -> Unit = {
        selectPiece(null)
        possibleMoves = listOf()
        checkCheckCheck()
        changeTurn()
    }

    private fun deleteAction() = ioToUnit {
        clearActionsUseCase.execute()
    }

    init {
        deleteAction()
    }

    fun startGame(player1: String, player2: String, gameType: GameType, gameId: Long) {
        whiteName = player1
        blackName = player2
        this.gameType = gameType
        this.gameId = gameId

        val initialTime = when (gameType) {
            GameType.SIXTY -> 60 * 60_000L
            GameType.THIRTY -> 30 * 60_000L
            GameType.FIFTEEN -> 15 * 60_000L
            GameType.TEN -> 10 * 60_000L
            GameType.FIVE_THREE -> 5 * 60_000L
            GameType.FIVE -> 5 * 60_000L
            GameType.THREE_TWO -> 3 * 60_000L
            GameType.THREE -> 3 * 60_000L
            GameType.ONE -> 60_000L
        }

        whiteTimeRemaining = initialTime
        blackTimeRemaining = initialTime
        startClock()
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
        possibleMoves = emptyList()
        println("Turn changed: White's Turn = $isWhiteTurn")

    }

    private fun checkCheckCheck() {
        val currentWhiteKing = whiteKing
        val currentBlackKing = blackKing

        if (currentWhiteKing == null || currentBlackKing == null) return

        val whiteResult = board.checkCheck(currentWhiteKing, board)
        val blackResult = board.checkCheck(currentBlackKing, board)

        whiteInCheck = whiteResult.first
        blackInCheck = blackResult.second

        if(whiteInCheck || blackInCheck) { getLineOfAttack() }

        viewModelScope.launch {
            if (whiteInCheck && board.isCheckmate(currentWhiteKing, board, lineOfAttack)) {

                winner = if (isWhiteTurn) blackName else whiteName
                viewModelScope.launch { endGameUseCase.execute(gameId!!, winner!!, whiteTimeRemaining!!, blackTimeRemaining!!) }
                navController?.navigate(Screen.MainMenu)

            } else if (blackInCheck && board.isCheckmate(currentBlackKing, board, lineOfAttack)) {

                winner = if (isWhiteTurn) blackName else whiteName
                viewModelScope.launch { endGameUseCase.execute(gameId!!, winner!!, whiteTimeRemaining!!, blackTimeRemaining!!) }
                navController?.navigate(Screen.MainMenu)

            }
        }
    }

    private fun getLineOfAttack() {
        when (lastMovedPiece?.type) {
            PieceType.PAWN -> {lineOfAttack = listOf(lastMovedPiece!!.position)}
            PieceType.KNIGHT -> {lineOfAttack = listOf(lastMovedPiece!!.position)}
            PieceType.BISHOP -> {lineOfAttack = getBishopLOA(lastMovedPiece)}
            PieceType.ROOK -> {lineOfAttack = getRookLOA(lastMovedPiece!!)}
            PieceType.QUEEN -> {lineOfAttack = getQueenLOA()}
            else -> {lineOfAttack = listOf()}
        }
    }

    private fun getQueenLOA(): List<Position> {
        return getBishopLOA(lastMovedPiece!!) + getRookLOA(lastMovedPiece!!)
    }

    private fun getBishopLOA(piece: ChessPiece?): List<Position> {
        val lineOfAttack: MutableList<Position> = mutableListOf()
        val piecePosition = piece!!.position
        val kingPosition = if(whiteInCheck) whiteKing!!.position else blackKing!!.position

        when {
            piecePosition.row < kingPosition.row && piecePosition.col < kingPosition.col -> {
                for (i in 1 until (kingPosition.row - piecePosition.row)) {
                    lineOfAttack.add(Position(piecePosition.row + i, piecePosition.col + i, FieldState.VALID))
                }
                lineOfAttack.add(piecePosition)
            }

            piecePosition.row < kingPosition.row && piecePosition.col > kingPosition.col -> {
                for (i in 1 until (kingPosition.row - piecePosition.row)) {
                    lineOfAttack.add(Position(piecePosition.row + i, piecePosition.col - i, FieldState.VALID))
                }
                lineOfAttack.add(piecePosition)
            }

            piecePosition.row > kingPosition.row && piecePosition.col < kingPosition.col -> {
                for (i in 1 until (piecePosition.row - kingPosition.row)) {
                    lineOfAttack.add(Position(piecePosition.row - i, piecePosition.col + i, FieldState.VALID))
                }
                lineOfAttack.add(piecePosition)
            }

            piecePosition.row > kingPosition.row && piecePosition.col > kingPosition.col -> {
                for (i in 1 until (piecePosition.row - kingPosition.row)) {
                    lineOfAttack.add(Position(piecePosition.row - i, piecePosition.col - i, FieldState.VALID))
                }
                lineOfAttack.add(piecePosition)
            }
        }
        return lineOfAttack
    }

    private fun getRookLOA(piece: ChessPiece): List<Position> {
        val lineOfAttack: MutableList<Position> = mutableListOf()
        val piecePosition = piece.position
        val kingPosition = if(whiteInCheck) whiteKing!!.position else blackKing!!.position

        when {
            piecePosition.row == kingPosition.row -> {
                //rook on left of king
                if(piecePosition.col < kingPosition.col) {
                    for (i in piecePosition.col + 1 until kingPosition.col) {
                        lineOfAttack.add(Position(piecePosition.row, i, FieldState.VALID))
                    }
                    lineOfAttack.add(piecePosition)
                }
                //rook on right of king
                else {
                    for (i in piecePosition.col - 1 downTo kingPosition.col) {
                        lineOfAttack.add(Position(piecePosition.row, i, FieldState.VALID))
                    }
                    lineOfAttack.add(piecePosition)
                }
            }

            piecePosition.col == kingPosition.col -> {
                //rook on top of king
                if(piecePosition.row < kingPosition.row) {
                    for (i in piecePosition.row + 1 until kingPosition.row) {
                        lineOfAttack.add(Position(i, piecePosition.col, FieldState.VALID))
                    }
                    lineOfAttack.add(piecePosition)
                }
                //rook on bottom of king
                else {
                    for (i in piecePosition.row - 1 downTo kingPosition.row) {
                        lineOfAttack.add(Position(i, piecePosition.col, FieldState.VALID))
                    }
                    lineOfAttack.add(piecePosition)
                }
            }
        }
        return lineOfAttack.filter { it.row in 0..7 && it.col in 0..7 } ?: listOf()
    }

    private fun _isPromotionPossible(piece: ChessPiece?, clickedSquare: Position?): Boolean {
        return when {
            piece == null || clickedSquare == null -> false
            piece.type == PieceType.PAWN && piece.movesMade >= 4 && (clickedSquare.row == 0 || clickedSquare.row == 7) -> piece.isWhite() && piece.position.row == 6 || !piece.isWhite() && piece.position.row == 1

            else -> false
        }
    }

    val isPromotionPossible: Boolean
        get() = _isPromotionPossible(selectedPiece, clickedSquare)

    fun handleSquareClick(row: Int, col: Int) {

        clickedSquare = Position(row, col, FieldState.EMPTY)
        val clickedPiece = this.boardArray[row][col]
        if (clickedPiece == selectedPiece) {selectedPiece = clickedPiece; return }

        if(!_isPromotionPossible(selectedPiece, clickedSquare)) {
            when {

                selectedPiece != null && clickedPiece == null && possibleMoves.contains(
                    Position(row, col, FieldState.VALID)
                ) -> { executeMove(row, col) }

                (selectedPiece != null && clickedPiece != null && possibleMoves.contains(
                    Position(row, col, FieldState.ATTACK)
                )) -> { executeAttack(row, col, clickedPiece) }

                else -> {
                    selectPiece(clickedPiece)
                    val updatedSelectedPiece =
                        if (clickedPiece?.color == selectedPiece?.color) clickedPiece else selectedPiece

                    val params = MovePieceUseCase.Params.create(
                        piece = updatedSelectedPiece, board = board, whiteInCheck, blackInCheck, lineOfAttack
                    )
                    ioToUi(io = { movePieceUseCase.execute(params) }, ui = {
                        possibleMoves = checkMoveParser(
                            whiteInCheck,
                            blackInCheck,
                            updatedSelectedPiece?.getPossibleMoves(board) ?: emptyList()
                        )
                    })
                }
            }
            checkCheckCheck()
        }
    }

    private fun executeMove(row: Int, col: Int) {
        val oldPosition = selectedPiece?.position

        val isCastle = board.move(
            selectedPiece!!,
            Position(row, col, FieldState.VALID),
            whiteInCheck,
            blackInCheck
        )
        lastMovedPiece = selectedPiece
        checkCheckCheck()
        val colorInCheck: PieceColor? =
            if (whiteInCheck) PieceColor.WHITE else if (blackInCheck) PieceColor.BLACK else null

        if (isCastle) {
            logAction(
                Action(
                    turnId = actionList.value.size + 1,
                    gameId = 1, // TODO Make this gameId auto become the current games id from db
                    selectedPiece!!,
                    ActionType.CASTLE,
                    time = System.currentTimeMillis(),
                    originalPosition = selectedPiece!!.position,
                    Position(row + 1, col, FieldState.VALID),
                    null,
                    null,
                    col < oldPosition!!.col,
                    colorInCheck
                )
            )
        } else {
            logAction(
                Action(
                    turnId = actionList.value.size + 1,
                    gameId = 1, // TODO Make this gameId auto become the current games id from db
                    selectedPiece!!,
                    ActionType.MOVE,
                    time = System.currentTimeMillis(),
                    originalPosition = oldPosition!!,
                    Position(row + 1, col, FieldState.VALID),
                    null,
                    null,
                    null,
                    colorInCheck
                )
            )
        }
        changeTurn()
    }

    private fun executeAttack(row: Int, col: Int, clickedPiece: ChessPiece?) {
        val oldPosition = selectedPiece?.position
        board.attack(selectedPiece!!, Position(row, col, FieldState.ATTACK))
        lastMovedPiece = selectedPiece
        checkCheckCheck()
        val colorInCheck: PieceColor? =
            if (whiteInCheck) PieceColor.WHITE else if (blackInCheck) PieceColor.BLACK else null
        logAction(
            Action(
                turnId = actionList.value.size + 1,
                gameId = 1, // TODO Make this gameId auto become the current games id from db
                selectedPiece!!,
                ActionType.ATTACK,
                time = System.currentTimeMillis(),
                originalPosition = oldPosition!!,
                Position(row, col, FieldState.ATTACK),
                killedPiece = clickedPiece,
                null,
                null,
                colorInCheck = colorInCheck
            )
        )
        changeTurn()
    }

    private fun checkMoveParser(
        whiteInCheck: Boolean,
        blackInCheck: Boolean,
        allPossibleMoves: List<Position>
    ): List<Position> {
        val attackerPosition = lastMovedPiece?.position
        val isKing = selectedPiece == whiteKing || selectedPiece == blackKing

        val validCandidates = when {
            whiteInCheck && !isKing -> {
                allPossibleMoves.filter {
                    board.blockCheck(it, lineOfAttack) ||
                            (attackerPosition != null && it.row == attackerPosition.row && it.col == attackerPosition.col)
                }
            }
            blackInCheck && !isKing -> {
                allPossibleMoves.filter {
                    board.blockCheck(it, lineOfAttack) ||
                            (attackerPosition != null && it.row == attackerPosition.row && it.col == attackerPosition.col)
                }
            }
            else -> allPossibleMoves
        }

        return if (isKing) {
            validCandidates.map { move ->
                if (board.isLegalMove(selectedPiece!!, move)) {
                    move
                } else {
                    move.copy(type = FieldState.BLOCKED)
                }
            }
        } else {
            validCandidates.filter { board.isLegalMove(selectedPiece!!, it) }
        }
    }

    fun forfeit() {
        winner = if (isWhiteTurn) blackName else whiteName
        viewModelScope.launch {
            endGameUseCase.execute(gameId!!, winner!!, whiteTimeRemaining!!, blackTimeRemaining!!)
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                if (isWhiteTurn) {
                    whiteTimeRemaining = (whiteTimeRemaining!! - 1000L)
                    println("${whiteTimeRemaining}")
                    if (whiteTimeRemaining!! < 1000L ) {
                        try {
                            endGameUseCase.execute(gameId!!, blackName!!, whiteTimeRemaining!!, blackTimeRemaining!!)
                        } catch (e: Exception) {
                            Log.e("GameViewModel", "Error in forfeit: ${e.message}")
                        }
                    }
                } else {
                    blackTimeRemaining = (blackTimeRemaining!! - 1000L)
                    if (blackTimeRemaining!! < 1000L ) {
                        endGameUseCase.execute(gameId!!, whiteName!!, whiteTimeRemaining!!, blackTimeRemaining!!)

                    }
                }
            }
        }
    }


    fun logAction(action: Action) {
        viewModelScope.launch {
            saveActionUseCase(action)
        }
    }


}