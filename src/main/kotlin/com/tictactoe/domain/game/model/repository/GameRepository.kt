package com.tictactoe.domain.game.model.repository

import com.tictactoe.domain.game.model.Game
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
}