package com.tictactoe.domain.game.model

import com.tictactoe.domain.game.model.enums.Mark
import com.tictactoe.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "moves")
class Move(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    val game: Game,

    val row: Int,
    val col: Int,

    @Enumerated(EnumType.STRING)
    val mark: Mark
) : BaseEntity()