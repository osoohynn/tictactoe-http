package com.tictactoe.domain.game.model

import com.tictactoe.domain.game.model.enums.Mark
import com.tictactoe.domain.user.domain.entity.User
import com.tictactoe.global.common.BaseEntity
import jakarta.persistence.*

@Entity
class Player (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @Enumerated(EnumType.STRING)
    val mark: Mark,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    val game: Game,
) : BaseEntity()