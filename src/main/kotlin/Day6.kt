package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day6.solvePart1()}")
    println("Part 2: ${Day6.solvePart2()}")
}

object Day6 {

    fun solvePart1(): Int {
        val field = Field(Day5::class.java.classLoader.getResource("day6.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.toCharArray().toList() }
            ?: error("Can't read input"))

        val visited = mutableSetOf<Coordinate>()
        var guardPosition = findStart(field)
        var direction = Direction.UP

        while (field.inRange(guardPosition)) {
            visited.add(guardPosition)
            val next = guardPosition.move(direction)
            if (field.isObstacle(next)) {
                direction = getNextDirection(direction)
            } else {
                guardPosition = next
            }
        }

        return visited.size
    }

    private fun getNextDirection(direction: Direction): Direction {
        var direction1 = direction
        direction1 = when (direction1) {
            Direction.UP -> Direction.RIGHT
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            Direction.RIGHT -> Direction.DOWN
        }
        return direction1
    }

    fun solvePart2(): Int {
        val field = Field(Day5::class.java.classLoader.getResource("day6.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.toCharArray().toList() }
            ?: error("Can't read input"))

        val startPositon = findStart(field)
        val startDirection = Direction.UP

        var cycleProducingCount = 0
        for (y in field.field.indices) {
            for (x in field.field[y].indices) {
                var current = Coordinate(x, y)
                if (!field.isObstacle(current)) {
                    val newField = field.copyWithObstacle(current)
                    if (isCycle(startPositon, startDirection, newField)) {
                        cycleProducingCount++
                    }
                }
            }
        }

        return cycleProducingCount
    }

    private fun isCycle(startPosition: Coordinate,
                        startDirection: Direction,
                        field: Field) : Boolean {
        val visited = mutableSetOf<State>()
        var guardPosition = startPosition
        var direction = startDirection

        while (field.inRange(guardPosition)) {
            val current = State(guardPosition, direction)
            if (visited.contains(current)) {
                return true
            }
            visited.add(current)
            val next = guardPosition.move(direction)
            if (field.isObstacle(next)) {
                direction = getNextDirection(direction)
            } else {
                guardPosition = next
            }
        }
        return false
    }

    data class State(
        val coordinate: Coordinate,
        val direction: Direction
    ) {

    }

    data class Coordinate(
        val x: Int, // left-right
        val y: Int, // up-down
    ) {

        fun move(direction: Direction): Coordinate {
            return when (direction) {
                Direction.UP -> Coordinate(x, y - 1)
                Direction.DOWN -> Coordinate(x, y + 1)
                Direction.LEFT -> Coordinate(x - 1, y)
                Direction.RIGHT -> Coordinate(x + 1, y)
            }
        }
    }

    data class Field(
        val field: List<List<Char>>
    ) {
        fun copyWithObstacle(coordinate: Coordinate): Field {
            val newField = field.map { it.toMutableList() }.toMutableList()
            newField[coordinate.y][coordinate.x] = '#'
            return Field(newField)
        }

        fun inRange(coordinate: Coordinate): Boolean {
            return coordinate.y in field.indices && coordinate.x in field[coordinate.y].indices
        }

        fun isObstacle(coordinate: Coordinate): Boolean {
            return if (inRange(coordinate)) {
                field[coordinate.y][coordinate.x] == '#'
            } else {
                false
            }
        }
    }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;
    }

    private fun findStart(field: Field): Coordinate {
        val y = field.field.indexOfFirst { it.contains('^') }
        val x = field.field[y].indexOfFirst { it == '^' }
        return Coordinate(x, y)
    }

}