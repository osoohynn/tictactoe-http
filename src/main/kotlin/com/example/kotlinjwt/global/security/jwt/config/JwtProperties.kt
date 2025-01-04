package com.example.kotlinjwt.global.security.jwt.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "spring.jwt")
data class JwtProperties (
    val issuer: String,
    val secretKey: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)