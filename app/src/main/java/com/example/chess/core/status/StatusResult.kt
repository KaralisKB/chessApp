package com.example.chess.core.status

class StatusResult<T> private constructor(val data: T?, val status: Status, val errorMessage: String?) {

    companion object {
        fun <T> success(data: T): StatusResult<T> = StatusResult(data, status = Status.SUCCESS, null)
        fun <T> loading(data: T?): StatusResult<T> = StatusResult(data, status = Status.LOADING, null)
        fun <T> error(error: Exception): StatusResult<T> = StatusResult(null, status = Status.ERROR, error.message)
    }
}