package com.tictactoe.global.security.jwt.error

import com.tictactoe.global.exception.CustomError

enum class JwtError(override val status: Int, override val message: String) :
    com.tictactoe.global.exception.CustomError {
    EXPIRED_TOKEN(401, "만료된 토큰입니다."),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    UNSUPPORTED_TOKEN(401, "지원하지 않는 토큰입니다."),
    MALFORMED_TOKEN(401, "잘못된 토큰입니다."),
    INVALID_TOKEN_TYPE(401, "유효하지 않은 토큰 타입입니다."),
}