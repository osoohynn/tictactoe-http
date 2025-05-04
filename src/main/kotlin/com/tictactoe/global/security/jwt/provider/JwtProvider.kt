package com.tictactoe.global.security.jwt.provider

import com.tictactoe.domain.auth.repository.RefreshTokenRepository
import com.tictactoe.domain.user.domain.entity.User
import com.tictactoe.domain.user.error.UserError
import com.tictactoe.domain.user.repository.UserRepository
import com.tictactoe.global.exception.CustomException
import com.tictactoe.global.security.CustomUserDetails
import com.tictactoe.global.security.jwt.config.JwtProperties
import com.tictactoe.global.security.jwt.dto.JwtResponse
import com.tictactoe.global.security.jwt.enums.JwtType
import com.tictactoe.global.security.jwt.error.JwtError
import io.jsonwebtoken.*
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.spec.SecretKeySpec


@Component
class JwtProvider(
    private val jwtProperties: com.tictactoe.global.security.jwt.config.JwtProperties,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: com.tictactoe.domain.auth.repository.RefreshTokenRepository
) {
    private val key = SecretKeySpec(
        jwtProperties.secretKey.toByteArray(Charsets.UTF_8),
        Jwts.SIG.HS512.key().build().algorithm
    )

    fun generateToken(user: User): JwtResponse {
        val now = Date()

        val accessToken = Jwts.builder()
            .header()
            .type(JwtType.ACCESS.name)
            .and()
            .subject(user.username)
            .issuedAt(now)
            .issuer(jwtProperties.issuer)
            .expiration(Date(now.time + jwtProperties.accessExp))
            .signWith(key)
            .compact()

        val refreshToken = Jwts.builder()
            .header()
            .type(JwtType.REFRESH.name)
            .and()
            .subject(user.username)
            .issuedAt(now)
            .issuer(jwtProperties.issuer)
            .expiration(Date(now.time + jwtProperties.refreshExp))
            .signWith(key)
            .compact()

        refreshTokenRepository.setRefreshToken(user.username, refreshToken)

        return JwtResponse(accessToken, refreshToken)
    }

    fun getUsername(token: String): String = getClaims(token).subject

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)
        val user = userRepository.findByUsername(claims.subject) ?: throw com.tictactoe.global.exception.CustomException(
            UserError.USER_NOT_FOUND
        )
        val details = CustomUserDetails(user)

        return UsernamePasswordAuthenticationToken(details, null, details.authorities)
    }

    fun extractToken(request: HttpServletRequest) =
        request.getHeader("Authorization")?.removePrefix("Bearer ")

    fun getType(token: String) = JwtType.valueOf(
        Jwts.parser()
            .verifyWith(key)
            .requireIssuer(jwtProperties.issuer)
            .build()
            .parseSignedClaims(token)
            .header.type
    )

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser()
                .verifyWith(key)
                .requireIssuer(jwtProperties.issuer)
                .build()
                .parseSignedClaims(token)
                .payload
        } catch (e: ExpiredJwtException) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.EXPIRED_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.UNSUPPORTED_TOKEN)
        } catch (e: MalformedJwtException) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.MALFORMED_TOKEN)
        } catch (e: SecurityException) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.INVALID_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw com.tictactoe.global.exception.CustomException(com.tictactoe.global.security.jwt.error.JwtError.INVALID_TOKEN)
        }
    }
}