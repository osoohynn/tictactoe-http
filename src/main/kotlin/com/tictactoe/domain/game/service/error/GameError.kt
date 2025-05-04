package com.tictactoe.domain.game.service.error

import com.tictactoe.global.exception.CustomError

enum class GameError(override val status: Int, override val message: String) : CustomError {
    GAME_NOT_FOUND(404, "찾을 수 없는 게임입니다."),
    TOO_MANY_PLAYER(400, "참여할 수 없는 게임입니다."),
    GAME_ALREADY_STARTED(400, "이미 시작한 게임은 나갈 수 없습니다.")
}