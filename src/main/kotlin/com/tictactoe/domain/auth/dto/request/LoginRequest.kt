package com.tictactoe.domain.auth.dto.request

data class LoginRequest(
    val username: String,
    val password: String
)