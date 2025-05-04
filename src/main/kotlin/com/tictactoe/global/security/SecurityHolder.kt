package com.tictactoe.global.security

import com.tictactoe.domain.user.domain.entity.User
import com.tictactoe.domain.user.error.UserError
import com.tictactoe.domain.user.repository.UserRepository
import com.tictactoe.global.exception.CustomException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component


@Component
class SecurityHolder(
    private val userRepository: UserRepository,
) {
    val user: User
        get() = userRepository.findByUsername(SecurityContextHolder.getContext().authentication.name)
            ?: throw com.tictactoe.global.exception.CustomException(UserError.USER_NOT_FOUND)
}