package com.tictactoe.global.exception

data class ErrorResponse(
    val code: String,
    val status: Int,
    val message: String
) {
    companion object {
        fun of(exception: com.tictactoe.global.exception.CustomException): ErrorResponse {
            return ErrorResponse(
                code = exception.code,
                status = exception.status,
                message = exception.message
            )
        }
    }
}
