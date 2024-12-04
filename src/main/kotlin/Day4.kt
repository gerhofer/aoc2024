package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day4.solvePart1()}")
    println("Part 2: ${Day4.solvePart2()}")
}

object Day4 {

    fun solvePart1(): Int {
        val letterMap = Day4::class.java.classLoader.getResource("day4-test.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split("").filter { it.isNotBlank() }.map { it[0] } }
            ?: error("Can't read input")

        val horizontal = XmasMatch(
            { pos -> Position(pos.row + 1, pos.col) },
            { pos -> Position(pos.row + 2, pos.col) },
            { pos -> Position(pos.row + 3, pos.col) }
        )

        val vertical = XmasMatch(
            { pos -> Position(pos.row, pos.col + 1) },
            { pos -> Position(pos.row, pos.col + 2) },
            { pos -> Position(pos.row, pos.col + 3) }
        )

        val diagonal = XmasMatch(
            { pos -> Position(pos.row + 1, pos.col + 1) },
            { pos -> Position(pos.row + 2, pos.col + 2) },
            { pos -> Position(pos.row + 3, pos.col + 3) }
        )

        val otherDiagonal = XmasMatch(
            { pos -> Position(pos.row + 1, pos.col - 1) },
            { pos -> Position(pos.row + 2, pos.col - 2) },
            { pos -> Position(pos.row + 3, pos.col - 3) }
        )

        var count = 0
        for (row in letterMap.indices) {
            for (col in letterMap[row].indices) {
                val currentPosition = Position(row, col)
                if (horizontal.matchesXmas(letterMap, currentPosition)) {
                    count++
                }
                if (vertical.matchesXmas(letterMap, currentPosition)) {
                    count++
                }
                if (diagonal.matchesXmas(letterMap, currentPosition)) {
                    count++
                }
                if (otherDiagonal.matchesXmas(letterMap, currentPosition)) {
                    count++
                }
            }
        }
        return count
    }

    fun solvePart2(): Int {
        val letterMap = Day4::class.java.classLoader.getResource("day4.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split("").filter { it.isNotBlank() }.map { it[0] } }
            ?: error("Can't read input")

        var count = 0
        for (row in letterMap.indices) {
            for (col in letterMap[row].indices) {
                if (matchesX(letterMap, Position(row, col))) {
                    count++
                }
            }
        }
        return count
    }

    data class Position(val row: Int, val col: Int) {

        fun get(crossword: List<List<Char>>): Char {
            if (row in crossword.indices && col in crossword[row].indices) {
                return crossword[row][col]
            }
            return '.'
        }

    }

    data class XmasMatch(
        val second: (position: Position) -> Position,
        val third: (position: Position) -> Position,
        val fourth: (position: Position) -> Position
    ) {
        fun matchesXmas(crossword: List<List<Char>>, startPosition: Position): Boolean {
            val first = startPosition.get(crossword)
            val second = second(startPosition).get(crossword)
            val third = third(startPosition).get(crossword)
            val fourth = fourth(startPosition).get(crossword)
            return (first == 'X' && second == 'M' && third == 'A' && fourth == 'S')
                    || (first == 'S' && second == 'A' && third == 'M' && fourth == 'X')
        }
    }

    val mAndS = setOf('M', 'S')

    fun matchesX(crossword: List<List<Char>>, startPosition: Position): Boolean {
        val leftTop = startPosition.get(crossword)
        val leftBottom = Position(startPosition.row + 2, startPosition.col).get(crossword)
        val middle = Position(startPosition.row + 1, startPosition.col + 1).get(crossword)
        val rightTop = Position(startPosition.row, startPosition.col + 2).get(crossword)
        val rightBottom = Position(startPosition.row + 2, startPosition.col + 2).get(crossword)
        return middle == 'A'
                && setOf(leftTop, rightBottom).containsAll(mAndS)
                && setOf(rightTop, leftBottom).containsAll(mAndS)
    }
}