package com.tictactoe.global.exception

class CustomException(val error: com.tictactoe.global.exception.CustomError, vararg args: String) : RuntimeException() {
    val code = (error as Enum<*>).name
    val status = error.status
    override val message = error.message.format(*args)
}