package com.example.chess.model

data class Position(val row: Int, val col: Int, val type: FieldState) {

    constructor(coords: Pair<Int, Int>, type: FieldState) : this(
        row = coords.first,
        col = coords.second,
        type = type
    )
}