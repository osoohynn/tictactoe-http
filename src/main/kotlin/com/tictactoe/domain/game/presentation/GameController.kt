package com.tictactoe.domain.game.presentation

import com.tictactoe.domain.game.presentation.dto.request.CreateGameRequest
import com.tictactoe.domain.game.presentation.dto.request.PlayMoveRequest
import com.tictactoe.domain.game.presentation.dto.response.GameResponse
import com.tictactoe.domain.game.presentation.dto.response.GameStateResponse
import com.tictactoe.domain.game.service.GameService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/games")
class GameController (
    private val gameService: GameService
) {
    @PostMapping
    fun createGame(@RequestBody req: CreateGameRequest) {
        gameService.createGame(req)
    }

    /** 게임 참가 */
    @PostMapping("/{gameId}/join")
    fun joinGame(@PathVariable gameId: Long) {
        gameService.joinGame(gameId)
    }

    /** 레디 상태, 게임 시작 */
    @PostMapping("/{gameId}/start")
    fun startGame(@PathVariable gameId: Long) {
        gameService.startGame(gameId)
    }

    /** 수 두기 */
    @PostMapping("/{gameId}/move")
    fun playMove(
        @PathVariable gameId: Long,
        @RequestBody req: PlayMoveRequest) {
        gameService.playMove(gameId, req.row, req.col)
    }

    /** 모든 게임 조회 */
    @GetMapping
    fun listGames(): List<GameResponse> {
        return gameService.getGames()
    }

    /** 단일 게임 상태 조회 */
    @GetMapping("/{gameId}")
    fun getGameState(@PathVariable gameId: Long): GameStateResponse =
        gameService.getGameState(gameId)
}