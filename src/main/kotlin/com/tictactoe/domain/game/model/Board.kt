package com.tictactoe.domain.game.model

import com.tictactoe.domain.game.model.enums.GameResult
import com.tictactoe.domain.game.model.enums.Mark

class Board {
    private val cells: Array<Array<Mark>> = Array(3) { Array(3) { Mark.EMPTY } }

    /** 지정 위치에 표시 */
    fun placeMark(row: Int, col: Int, mark: Mark) {
        require(cells[row][col] == Mark.EMPTY) { "Cell is already occupied" }
        cells[row][col] = mark
    }

    /** 현재 보드 스냅샷 반환 */
    fun snapshot(): List<List<Mark>> = cells.map { it.toList() }

    /** 승부 상태 판정 */
    fun evaluate(): GameResult {
        // 가로, 세로 체크
        for (i in 0..2) {
            if (cells[i][0] != Mark.EMPTY && cells[i][0] == cells[i][1] && cells[i][1] == cells[i][2])
                return if (cells[i][0] == Mark.X) GameResult.X_WON else GameResult.O_WON
            if (cells[0][i] != Mark.EMPTY && cells[0][i] == cells[1][i] && cells[1][i] == cells[2][i])
                return if (cells[0][i] == Mark.X) GameResult.X_WON else GameResult.O_WON
        }
        // 대각선 체크
        if (cells[0][0] != Mark.EMPTY && cells[0][0] == cells[1][1] && cells[1][1] == cells[2][2])
            return if (cells[0][0] == Mark.X) GameResult.X_WON else GameResult.O_WON
        if (cells[0][2] != Mark.EMPTY && cells[0][2] == cells[1][1] && cells[1][1] == cells[2][0])
            return if (cells[0][2] == Mark.X) GameResult.X_WON else GameResult.O_WON
        // 진행 중인지 확인
        if (cells.any { row -> row.any { it == Mark.EMPTY } }) {
            return GameResult.IN_PROGRESS
        }
        // 모두 채워졌지만 승자 없음
        return GameResult.DRAW
    }
}