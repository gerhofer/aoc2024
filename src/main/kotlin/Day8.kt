package org.example

import kotlin.math.abs

fun main(args: Array<String>) {
    println("Part 1: ${Day8.solvePart1()}")
    println("Part 2: ${Day8.solvePart2()}")
}

object Day8 {

    fun solvePart1(): Int {
        val field = Day8::class.java.classLoader.getResource("day8.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split("").filter { it.isNotBlank() } }
            ?: error("Can't read input")

        val occurances = parseOccurances(field).filter { it.key != "." }
        val antidotes = mutableSetOf<Coordinate>()
        for ((antenna, locations) in occurances) {
            for (firstIdx in locations.indices) {
                for (otherIdx in locations.indices) {
                    if (firstIdx != otherIdx) {
                        val first = locations[firstIdx]
                        val other = locations[otherIdx]

                        val currentAntidotes = getAntiDote(first.coordinate, other.coordinate)
                        //println(currentAntidotes)
                        antidotes.addAll(currentAntidotes)
                    }
                }
            }
        }

        return antidotes.count { it.y in field.indices && it.x in field[0].indices }
    }

    fun getAntiDote(first: Coordinate, second: Coordinate): Set<Coordinate> {
        val dir = first.getRelativePositionToOther(second)
        val xDistance = abs(first.x - second.x)
        val yDistance = abs(first.y - second.y)
        return when (dir) {
            RelativePosition.LEFT_UP -> setOf(
                Coordinate(first.x - xDistance, first.y - yDistance),
                Coordinate(second.x + xDistance, second.y + yDistance)
            )

            RelativePosition.RIGHT_UP -> setOf(
                Coordinate(first.x + xDistance, first.y - yDistance),
                Coordinate(second.x - xDistance, second.y + yDistance)
            )

            RelativePosition.LEFT_DOWN -> setOf(
                Coordinate(first.x - xDistance, first.y + yDistance),
                Coordinate(second.x + xDistance, second.y - yDistance)
            )

            RelativePosition.RIGHT_DOWN -> setOf(
                Coordinate(first.x + xDistance, first.y + yDistance),
                Coordinate(second.x - xDistance, second.y - yDistance)
            )
        }
    }

    fun getAllAntiDotes(first: Coordinate, second: Coordinate, field: List<List<String>>): Set<Coordinate> {
        val dir = first.getRelativePositionToOther(second)
        val xDistance = abs(first.x - second.x)
        val yDistance = abs(first.y - second.y)
        return when (dir) {
            RelativePosition.LEFT_UP -> moveUntilReach(field, first) { first ->
                Coordinate(
                    first.x - xDistance,
                    first.y - yDistance
                )
            } + moveUntilReach(field, second) { second ->
                Coordinate(
                    second.x + xDistance,
                    second.y + yDistance
                )
            }

            RelativePosition.RIGHT_UP -> moveUntilReach(field, first) { first ->
                Coordinate(first.x + xDistance, first.y - yDistance)
            } +
                    moveUntilReach(field, second) { second ->
                        Coordinate(second.x - xDistance, second.y + yDistance)
                    }

            RelativePosition.LEFT_DOWN -> moveUntilReach(field, first) { first ->
                Coordinate(first.x - xDistance, first.y + yDistance)
            } + moveUntilReach(field, second) { second ->
                Coordinate(second.x + xDistance, second.y - yDistance)
            }

            RelativePosition.RIGHT_DOWN -> moveUntilReach(field, first) { first ->
                Coordinate(first.x + xDistance, first.y + yDistance)
            } + moveUntilReach(field, second) { second ->
                Coordinate(second.x - xDistance, second.y - yDistance)
            }
        }
    }

    fun moveUntilReach(field: List<List<String>>, start: Coordinate, modifier: (Coordinate) -> Coordinate): Set<Coordinate> {
        val result = mutableSetOf<Coordinate>()
        var current = start
        while (current.x in field[0].indices && current.y in field.indices) {
            result.add(current)
            val next = modifier(current)
            current = next
        }
        return result
    }

    fun solvePart2(): Int {
        val field = Day8::class.java.classLoader.getResource("day8.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split("").filter { it.isNotBlank() } }
            ?: error("Can't read input")

        val occurances = parseOccurances(field).filter { it.key != "." }
        val antidotes = mutableSetOf<Coordinate>()
        for ((antenna, locations) in occurances) {
            for (firstIdx in locations.indices) {
                for (otherIdx in locations.indices) {
                    if (firstIdx != otherIdx) {
                        val first = locations[firstIdx]
                        val other = locations[otherIdx]

                        val currentAntidotes = getAllAntiDotes(first.coordinate, other.coordinate, field)
                        //println(currentAntidotes)
                        antidotes.addAll(currentAntidotes)
                    }
                }
            }
        }

        return antidotes.count { it.y in field.indices && it.x in field[0].indices }

    }

    fun parseOccurances(map: List<List<String>>): Map<String, List<CoordinateWithValue>> {
        return map
            .mapIndexed { y, row ->
                row.mapIndexed { x, value -> CoordinateWithValue(value, Coordinate(x, y)) }
            }
            .flatten()
            .groupBy { it.value }
            .filter { it.key != "." }
    }

    enum class RelativePosition {
        LEFT_UP, // \
        RIGHT_UP, // /
        LEFT_DOWN,
        RIGHT_DOWN
    }

    data class Coordinate(val x: Int, val y: Int) {
        fun getRelativePositionToOther(other: Coordinate): RelativePosition {
            if (x < other.x) { // this is left
                if (y < other.y) { // this is up
                    return RelativePosition.LEFT_UP
                } else {
                    return RelativePosition.LEFT_DOWN
                }
            } else { // this is same or right
                if (y < other.y) { // this is up
                    return RelativePosition.RIGHT_UP
                } else {
                    return RelativePosition.RIGHT_DOWN
                }
            }
        }
    }

    data class CoordinateWithValue(val value: String, val coordinate: Coordinate)


}