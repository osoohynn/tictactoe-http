package com.tictactoe.domain.auth.controller

import com.tictactoe.domain.auth.dto.request.LoginRequest
import com.tictactoe.domain.auth.dto.request.ReissueRequest
import com.tictactoe.domain.auth.dto.request.SignUpRequest
import com.tictactoe.domain.auth.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/sign-up")
    fun signup(@RequestBody request: SignUpRequest) = authService.signup(request)

    @PostMapping("/sign-in")
    fun login(@RequestBody request: LoginRequest) = authService.login(request)

    @PostMapping("/reissue")
    fun reissue(@RequestBody request: ReissueRequest) = authService.reissue(request)
}