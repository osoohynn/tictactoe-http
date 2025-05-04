package com.tictactoe.domain.auth.service

import com.tictactoe.domain.auth.dto.request.LoginRequest
import com.tictactoe.domain.auth.dto.request.ReissueRequest
import com.tictactoe.domain.auth.dto.request.SignUpRequest
import com.tictactoe.domain.auth.repository.RefreshTokenRepository
import com.tictactoe.domain.user.domain.entity.User
import com.tictactoe.domain.user.domain.enums.UserRole
import com.tictactoe.domain.user.error.UserError
import com.tictactoe.domain.user.repository.UserRepository
import com.tictactoe.global.exception.CustomException
import com.tictactoe.global.security.jwt.dto.JwtResponse
import com.tictactoe.global.security.jwt.enums.JwtType
import com.tictactoe.global.security.jwt.error.JwtError
import com.tictactoe.global.security.jwt.provider.JwtProvider
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
    private val refreshTokenRepository: com.tictactoe.domain.auth.repository.RefreshTokenRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun signup(request: SignUpRequest) {
        val username = request.username
        val password = passwordEncoder.encode(request.password)

        if (userRepository.existsByUsername(username)) {
            throw com.tictactoe.global.exception.CustomException(UserError.USERNAME_DUPLICATED)
        }

        val user = User(
            username = username,
            password = password,
            role = UserRole.USER
        )

        userRepository.save(user)
    }

    @Transactional
    fun login(request: LoginRequest): JwtResponse {
        val username = request.username
        val password = request.password

        val user = userRepository.findByUsername(username) ?: throw com.tictactoe.global.exception.CustomException(
            UserError.USER_NOT_FOUND
        )

        if (!passwordEncoder.matches(password, user.password)) {
            throw com.tictactoe.global.exception.CustomException(UserError.PASSWORD_NOT_MATCH)
        }

        return jwtProvider.generateToken(user)
    }

    @Transactional
    fun reissue(request: ReissueRequest): JwtResponse {
        if (jwtProvider.getType(request.refreshToken) != JwtType.REFRESH) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.INVALID_TOKEN_TYPE)
        }

        val username = jwtProvider.getUsername(request.refreshToken)
        val user = userRepository.findByUsername(username) ?: throw com.tictactoe.global.exception.CustomException(
            UserError.USER_NOT_FOUND
        )
        val refreshToken =
            refreshTokenRepository.getRefreshToken(username) ?: throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.INVALID_TOKEN)

        if (refreshToken != request.refreshToken) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.INVALID_TOKEN)
        }

        return jwtProvider.generateToken(user)
    }
}