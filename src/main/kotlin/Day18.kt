package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day18.solvePart1()}")
    println("Part 2: ${Day18.solvePart2()}")
}

object Day18 {

    fun solvePart1(): Int {
        val size = 71
        val dangerousPositions = readAndParsePositions()
            .take(1024)
            .toSet()
        val start = Position(0, 0)
        val end = Position(size - 1, size - 1)
        val paths = mutableMapOf(Position(0, 0) to 0)

        performPossibleMoves(paths, dangerousPositions, start, end)

        return paths[end] ?: throw IllegalStateException("Could not reach end")
    }

    fun solvePart2(): String {
        val size = 71
        var currentSize = 1024
        val dangerousPositions = readAndParsePositions()
        val start = Position(0, 0)
        val end = Position(size - 1, size - 1)

        while (true) {
            val paths = mutableMapOf(Position(0, 0) to 0)
            performPossibleMoves(paths, dangerousPositions.take(currentSize).toSet(), start, end)
            if (paths[end] == null) {
                val pointCuttingOffEnd = dangerousPositions.get(currentSize-1)
                return "${pointCuttingOffEnd.x},${pointCuttingOffEnd.y}"
            }
            currentSize++
        }
    }

    fun performPossibleMoves(paths: MutableMap<Position, Int>, blocked: Set<Position>, current: Position, end: Position) {
        if (current == end) {
            return
        }
        val currentPath = paths[current] ?: 0
        performMovesIfBetter(current.left(), blocked, end, paths, currentPath)
        performMovesIfBetter(current.right(),blocked,  end, paths, currentPath)
        performMovesIfBetter(current.down(), blocked, end, paths, currentPath)
        performMovesIfBetter(current.up(), blocked, end, paths, currentPath)
    }

    private fun performMovesIfBetter(
        next: Position,
        blocked: Set<Position>,
        end: Position,
        paths: MutableMap<Position, Int>,
        currentPath: Int
    ) {
        if (!blocked.contains(next) && next.x in (0..end.x) && next.y in (0..end.y)) {
            var previousPath = paths[next]
            if (previousPath == null || previousPath > (currentPath + 1)) {
                paths[next] = currentPath + 1
                performPossibleMoves(paths, blocked, next, end)
            }
        }
    }

    private fun readAndParsePositions(): List<Position> {
        val positions = Day18::class.java.classLoader.getResource("day18.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { Position.fromString(it) }
            ?: error("Can't read input")
        return positions
    }

    data class Position(val x: Int, val y: Int) {

        fun left(): Position = Position(x-1, y)
        fun right(): Position = Position(x+1, y)
        fun up(): Position = Position(x, y-1)
        fun down(): Position = Position(x, y+1)

        companion object {
            fun fromString(string: String): Position {
                val (x, y) = string.split(",").map { it.toInt() }
                return Position(x, y)
            }
        }
    }

}