package com.tictactoe.domain.game.presentation.dto.request

data class PlayMoveRequest(
    val row: Int,
    val col: Int
)