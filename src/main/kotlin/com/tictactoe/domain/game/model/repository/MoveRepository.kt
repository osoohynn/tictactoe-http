package com.tictactoe.domain.game.model.repository

import com.tictactoe.domain.game.model.Game
import com.tictactoe.domain.game.model.Move
import org.springframework.data.jpa.repository.JpaRepository

interface MoveRepository : JpaRepository<Move, Long> {
    fun findByGameOrderByCreatedAtAsc(game: Game): List<Move>
}