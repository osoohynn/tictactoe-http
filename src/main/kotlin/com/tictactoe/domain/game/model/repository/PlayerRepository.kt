package com.tictactoe.domain.game.model.repository

import com.tictactoe.domain.game.model.Game
import com.tictactoe.domain.game.model.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<Player, Long> {
    fun countByGame(game: Game): Long
    fun findByGame(game: Game): List<Player>
}