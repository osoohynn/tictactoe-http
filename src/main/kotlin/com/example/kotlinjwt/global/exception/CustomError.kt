package com.example.kotlinjwt.global.exception

import org.springframework.http.HttpStatus

interface CustomError {
    val status: HttpStatus
    val message: String
}