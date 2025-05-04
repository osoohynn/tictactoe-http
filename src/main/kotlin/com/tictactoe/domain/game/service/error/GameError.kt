package com.tictactoe.domain.game.service.error

import com.tictactoe.global.exception.CustomError

enum class GameError(override val status: Int, override val message: String) : CustomError {
    GAME_NOT_FOUND(404, "찾을 수 없는 게임입니다."),
    TOO_MANY_PLAYER(400, "참여할 수 없는 게임입니다."),
    GAME_ALREADY_STARTED(400, "이미 시작한 게임은 나갈 수 없습니다."),
    CELL_OCCUPIED(400, "이미 놓은 위치입니다."),
    NOT_PLAYER(404, "게임 참여자가 아닙니다."),
    NOT_YOUR_TURN(400, "차례가 아닙니다."),
    GAME_NOT_STARTED(400, "게임이 시작하지 않았습니다.")
}