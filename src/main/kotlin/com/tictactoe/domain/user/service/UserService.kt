package com.tictactoe.domain.user.service

import com.tictactoe.domain.user.dto.response.UserResponse
import com.tictactoe.global.security.SecurityHolder
import org.springframework.stereotype.Service


@Service
class UserService(
    private val securityHolder: SecurityHolder
) {
    fun getMe() : UserResponse {
        val user = securityHolder.user

        return UserResponse.of(user)
    }
}