package com.example.chess.local.model

data class Position(val row: Int, val col: Int, var type: FieldState) {

    constructor(coords: Pair<Int, Int>, type: FieldState) : this(
        row = coords.first,
        col = coords.second,
        type = type
    )
}