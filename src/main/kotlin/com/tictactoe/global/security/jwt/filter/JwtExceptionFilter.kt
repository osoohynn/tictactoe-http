package com.tictactoe.global.security.jwt.filter

import com.tictactoe.global.exception.CustomException
import com.tictactoe.global.exception.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: com.tictactoe.global.exception.CustomException) {
            response.sendError(e)
        }
    }

    private fun HttpServletResponse.sendError(exception: com.tictactoe.global.exception.CustomException) {
        val error = exception.error
        status = error.status

        outputStream.use {
            it.write(objectMapper.writeValueAsBytes(ErrorResponse.of(exception)))
            it.flush()
        }
    }
}