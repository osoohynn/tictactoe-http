package com.tictactoe.domain.game.model

import com.tictactoe.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "games")
class Game (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    var isStarted: Boolean,
) : BaseEntity()