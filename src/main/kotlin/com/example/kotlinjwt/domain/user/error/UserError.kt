package com.example.kotlinjwt.domain.user.error

import com.example.kotlinjwt.global.exception.CustomError
import org.springframework.http.HttpStatus

enum class UserError(override val status: HttpStatus, override val message: String) : CustomError {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    USERNAME_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 유저네임입니다."),
}