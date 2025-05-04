package com.tictactoe.domain.game.presentation.dto.response

import com.tictactoe.domain.game.model.Game

data class GameResponse (
    val id: Long,
    val name: String,
    val playerCount: Long,
    val players: List<PlayerResponse>,
) {
    companion object {
        fun of(game: Game, playerCount: Long, players: List<PlayerResponse>): GameResponse {
            return GameResponse(
                id = game.id!!,
                name = game.name,
                playerCount = playerCount,
                players = players
            )
        }
    }
}