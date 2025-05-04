package com.tictactoe.domain.game.presentation.dto.response

import com.tictactoe.domain.game.model.enums.GameResult
import com.tictactoe.domain.game.model.enums.Mark

data class GameStateResponse(
    val board: List<List<Mark>>,
    val result: GameResult,
    val nextTurn: Mark
)
