package com.tictactoe.global.security

import com.tictactoe.domain.user.domain.entity.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User) : UserDetails {
    override fun getAuthorities() = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
    override fun getUsername() = user.username
    override fun getPassword() = user.password
}