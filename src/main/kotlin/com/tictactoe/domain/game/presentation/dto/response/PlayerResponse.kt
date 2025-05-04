package com.tictactoe.domain.game.presentation.dto.response

import com.tictactoe.domain.game.model.Player

data class PlayerResponse(
    val id: Long,
    val name: String,
) {
    companion object {
        fun of(player: Player): PlayerResponse = PlayerResponse(
            id = player.id!!,
            name = player.user.username
        )
    }
}