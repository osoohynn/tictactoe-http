package com.tictactoe.domain.game.service

import com.tictactoe.domain.game.model.Game
import com.tictactoe.domain.game.model.Player
import com.tictactoe.domain.game.model.enums.Mark
import com.tictactoe.domain.game.model.repository.GameRepository
import com.tictactoe.domain.game.model.repository.PlayerRepository
import com.tictactoe.domain.game.presentation.dto.request.CreateGameRequest
import com.tictactoe.domain.game.presentation.dto.response.GameResponse
import com.tictactoe.domain.game.presentation.dto.response.PlayerResponse
import com.tictactoe.domain.game.service.error.GameError
import com.tictactoe.global.exception.CustomException
import com.tictactoe.global.security.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService (
    private val gameRepository: GameRepository,
    private val playerRepository: PlayerRepository,
    private val securityHolder: SecurityHolder
) {
    @Transactional
    fun createGame(request: CreateGameRequest) {
        val user = securityHolder.user

        val game = Game(name = request.name, isStarted = false)
        gameRepository.save(game)

        val player = Player(user = user, mark = Mark.O, game = game)
        playerRepository.save(player)
    }

    @Transactional
    fun joinGame(gameId: Long) {
        val user = securityHolder.user
        val game = gameRepository.findByIdOrNull(gameId) ?: throw CustomException(GameError.GAME_NOT_FOUND)
        if (playerRepository.countByGame(game) > 1L) throw CustomException(GameError.TOO_MANY_PLAYER)

        val newMark = if (playerRepository.countByGame(game) == 1L) {
            val player = playerRepository.findByGame(game).first()
            val mark = player.mark
            if (mark.name == "O") Mark.X else Mark.O
        } else {
            Mark.O
        }
        val newPlayer = Player(user = user, mark = newMark, game = game)

        playerRepository.save(newPlayer)
    }

    @Transactional
    fun leaveGame(gameId: Long) {
        val user = securityHolder.user
        val game = gameRepository.findByIdOrNull(gameId) ?: throw CustomException(GameError.GAME_NOT_FOUND)
        if (game.isStarted) throw CustomException(GameError.GAME_ALREADY_STARTED)
        val player = playerRepository.findByGame(game)
        player.forEach{if (it.user.id == user.id) playerRepository.delete(it)}
    }

    @Transactional
    fun getGames(): List<GameResponse> {
        val games = gameRepository.findAll()
        return games.map { game -> GameResponse.of(
            game, playerRepository.countByGame(game = game),
            playerRepository.findByGame(game).map { PlayerResponse.of(it) }) }
    }

    @Transactional
    fun getGame(gameId: Long): GameResponse {
        val game = gameRepository.findByIdOrNull(gameId) ?: throw CustomException(GameError.GAME_NOT_FOUND)

        return GameResponse.of(
            game, playerRepository.countByGame(game),
            playerRepository.findByGame(game).map { PlayerResponse.of(it) })
    }
}