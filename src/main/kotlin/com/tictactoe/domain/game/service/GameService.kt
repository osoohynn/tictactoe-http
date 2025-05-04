package com.tictactoe.domain.game.service

import com.tictactoe.domain.game.model.Board
import com.tictactoe.domain.game.model.Game
import com.tictactoe.domain.game.model.Move
import com.tictactoe.domain.game.model.Player
import com.tictactoe.domain.game.model.enums.GameResult
import com.tictactoe.domain.game.model.enums.Mark
import com.tictactoe.domain.game.model.repository.GameRepository
import com.tictactoe.domain.game.model.repository.MoveRepository
import com.tictactoe.domain.game.model.repository.PlayerRepository
import com.tictactoe.domain.game.presentation.dto.request.CreateGameRequest
import com.tictactoe.domain.game.presentation.dto.response.GameResponse
import com.tictactoe.domain.game.presentation.dto.response.GameStateResponse
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
    private val securityHolder: SecurityHolder,
    private val moveRepository: MoveRepository
) {
    @Transactional
    fun createGame(request: CreateGameRequest) {
        val user = securityHolder.user

        val game = Game(name = request.name, isStarted = false)
        gameRepository.save(game)

        val player = Player(user = user, mark = Mark.O, game = game, isReady = false)
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
        val newPlayer = Player(user = user, mark = newMark, game = game, isReady = false)

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

    @Transactional
    fun startGame(gameId: Long) {
        val user = securityHolder.user
        val game = gameRepository.findById(gameId)
            .orElseThrow { CustomException(GameError.GAME_NOT_FOUND) }

        val player = playerRepository.findByGame(game)
            .firstOrNull { it.user.id == user.id }
            ?: throw CustomException(GameError.NOT_PLAYER)

        player.isReady = true
        playerRepository.save(player)

        // 모든 플레이어가 준비됐는지 확인 (2명 기준)
        val readyCount = playerRepository.findByGame(game).count { it.isReady }
        if (readyCount >= 2) {
            game.isStarted = true
            gameRepository.save(game)
        }
    }

    /**
     * 게임 진행
     * */

    /** 사용자 인증된 플레이어의 차례에만 수 두기 */
    @Transactional
    fun playMove(gameId: Long, row: Int, col: Int) {
        val user = securityHolder.user
        val game = gameRepository.findById(gameId)
            .orElseThrow { CustomException(GameError.GAME_NOT_FOUND) }

        if (!game.isStarted) throw CustomException(GameError.GAME_NOT_STARTED)

        val player = playerRepository.findByGame(game)
            .firstOrNull { it.user.id == user.id }
            ?: throw CustomException(GameError.NOT_PLAYER)

        val pastMoves = moveRepository.findByGameOrderByCreatedAtAsc(game)
        // 차례 계산
        val xCount = pastMoves.count { it.mark == Mark.X }
        val oCount = pastMoves.count { it.mark == Mark.O }
        val expectedMark = if (oCount > xCount) Mark.X else Mark.O
        if (player.mark != expectedMark) {
            throw CustomException(GameError.NOT_YOUR_TURN)
        }
        // 이미 둔 자리 확인
        if (pastMoves.any { it.row == row && it.col == col }) {
            throw CustomException(GameError.CELL_OCCUPIED)
        }
        moveRepository.save(Move(game = game, row = row, col = col, mark = player.mark))
    }

    /** 보드 상태, 승부 상태, 그리고 다음 차례 확인 */
    @Transactional(readOnly = true)
    fun getGameState(gameId: Long): GameStateResponse {
        val game = gameRepository.findById(gameId)
            .orElseThrow { CustomException(GameError.GAME_NOT_FOUND) }
        val board = Board()
        val pastMoves = moveRepository.findByGameOrderByCreatedAtAsc(game)
        pastMoves.forEach { board.placeMark(it.row, it.col, it.mark) }

        val result = board.evaluate()
        // 다음 차례 결정 (게임 진행 중일 때만)
        val nextTurn = when {
            result != GameResult.IN_PROGRESS -> Mark.EMPTY
            pastMoves.lastOrNull()?.mark == Mark.X -> Mark.O
            pastMoves.lastOrNull()?.mark == Mark.O -> Mark.X
            else -> Mark.O
        }
        return GameStateResponse(board.snapshot(), result, nextTurn)
    }
}