package com.tictactoe.domain.user.dto.response

import com.tictactoe.domain.user.domain.entity.User

data class UserResponse(
    val username: String,
) {
    companion object {
        fun of(user: User) = UserResponse(
            username = user.username,
        )
    }
}