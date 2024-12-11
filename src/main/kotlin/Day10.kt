package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day10.solvePart1()}")
    println("Part 2: ${Day10.solvePart2()}")
}

object Day10 {

    fun solvePart1(): Long {
        val field = Day10::class.java.classLoader.getResource("day10.txt")
            ?.readText()
            ?.split("\r\n")
            ?.mapIndexed { y, line ->
                line.split("").filter { it.isNotBlank() }
                    .mapIndexed { x, value -> TrailField(value.toInt(), x, y) }
            }
            ?: error("Can't read input")

        val trailHeads = field.flatten().filter { it.value == 0 }

        return trailHeads.sumOf { getScore(field, it) }
    }

    fun getScore(field: List<List<TrailField>>, start: TrailField): Long {
        val visited = mutableSetOf<TrailField>()
        moveRec(field, start, visited)
        return visited.count { it.value == 9 }.toLong()
    }

    fun moveRec(
        field: List<List<TrailField>>,
        current: TrailField,
        visited: MutableSet<TrailField>
    ) {
        if (current.y - 1 in field.indices && field[current.y - 1][current.x].value == current.value + 1) {
            val up = field[current.y - 1][current.x]
            if (!visited.contains(up)) {
                visited.add(up)
                moveRec(field, up, visited)
            }
        }

        if (current.y + 1 in field.indices && field[current.y + 1][current.x].value == current.value + 1) {
            val down = field[current.y + 1][current.x]
            if (!visited.contains(down)) {
                visited.add(down)
                moveRec(field, down, visited)
            }
        }

        if (current.x - 1 in field[0].indices && field[current.y][current.x - 1].value == current.value + 1) {
            val left = field[current.y][current.x - 1]
            if (!visited.contains(left)) {
                visited.add(left)
                moveRec(field, left, visited)
            }
        }

        if (current.x + 1 in field[0].indices && field[current.y][current.x + 1].value == current.value + 1) {
            val right = field[current.y][current.x + 1]
            if (!visited.contains(right)) {
                visited.add(right)
                moveRec(field, right, visited)
            }
        }
    }

    fun solvePart2(): Long {
        val field = Day10::class.java.classLoader.getResource("day10.txt")
            ?.readText()
            ?.split("\r\n")
            ?.mapIndexed { y, line ->
                line.split("").filter { it.isNotBlank() }
                    .mapIndexed { x, value -> TrailField(value.toInt(), x, y) }
            }
            ?: error("Can't read input")

        val trailHeads = field.flatten().filter { it.value == 0 }

        return trailHeads.sumOf { getScoreV2(field, it) }
    }

    fun getScoreV2(field: List<List<TrailField>>, start: TrailField): Long {
        val visited = mutableListOf<TrailField>()
        moveRecV2(field, start, visited)
        return visited.count { it.value == 9 }.toLong()
    }

    fun moveRecV2(
        field: List<List<TrailField>>,
        current: TrailField,
        visited: MutableList<TrailField>
    ) {
        if (current.y - 1 in field.indices && field[current.y - 1][current.x].value == current.value + 1) {
            val up = field[current.y - 1][current.x]
            visited.add(up)
            moveRecV2(field, up, visited)
        }

        if (current.y + 1 in field.indices && field[current.y + 1][current.x].value == current.value + 1) {
            val down = field[current.y + 1][current.x]
            visited.add(down)
            moveRecV2(field, down, visited)
        }

        if (current.x - 1 in field[0].indices && field[current.y][current.x - 1].value == current.value + 1) {
            val left = field[current.y][current.x - 1]
            visited.add(left)
            moveRecV2(field, left, visited)
        }

        if (current.x + 1 in field[0].indices && field[current.y][current.x + 1].value == current.value + 1) {
            val right = field[current.y][current.x + 1]
            visited.add(right)
            moveRecV2(field, right, visited)
        }
    }

    data class TrailField(val value: Int, val x: Int, val y: Int)

}