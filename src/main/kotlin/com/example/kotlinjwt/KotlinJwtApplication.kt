package com.example.kotlinjwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class KotlinJwtApplication

fun main(args: Array<String>) {
    runApplication<KotlinJwtApplication>(*args)
}