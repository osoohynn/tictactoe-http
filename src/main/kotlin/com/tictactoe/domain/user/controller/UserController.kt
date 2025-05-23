package com.tictactoe.domain.user.controller

import com.tictactoe.domain.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/users")
class UserController (
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getMe() = userService.getMe()
}