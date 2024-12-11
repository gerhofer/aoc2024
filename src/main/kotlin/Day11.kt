package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day11.solvePart1()}")
    println("Part 2: ${Day11.solvePart2()}")
}

object Day11 {

    fun solvePart1(): Long {
        val engravedStonesToCount = Day11::class.java.classLoader.getResource("day11.txt")
            ?.readText()
            ?.split(" ")
            ?.filter { it.isNotBlank() }
            ?.associateWith { 1L }
            ?: error("Can't read input")

        var currentGeneration = engravedStonesToCount
        repeat(25) {
            currentGeneration = getNextGeneration(currentGeneration)
        }

        return currentGeneration.values.sum()
    }

    fun getNextGeneration(engravedStonesToCount: Map<String, Long>): Map<String, Long> {
        val nextGeneration = mutableMapOf<String, Long>()
        for ((stone, count) in engravedStonesToCount) {
            if (stone == "0") {
                addCount(nextGeneration, "1", count)
            } else if (stone.length % 2 == 0) {
                addCount(nextGeneration, getFirstHalf(stone), count)
                addCount(nextGeneration, getSecondHalfWithoutLeadingZeros(stone), count)
            } else {
                addCount(nextGeneration, (stone.toLong()*2024).toString(), count)
            }
        }
        return nextGeneration
    }

    private fun addCount(nextGeneration: MutableMap<String, Long>, newStone: String, count: Long) {
        nextGeneration[newStone] = nextGeneration.getOrDefault(newStone, 0) + count
    }

    private fun getSecondHalfWithoutLeadingZeros(stone: String): String {
        var second = stone.substring(stone.length / 2)
        while (second.length > 1 && second.startsWith("0")) {
            second = second.substring(1)
        }
        return second
    }

    private fun getFirstHalf(stone: String) = stone.substring(0, stone.length / 2)

    fun solvePart2(): Long {
        val engravedStonesToCount = Day11::class.java.classLoader.getResource("day11.txt")
            ?.readText()
            ?.split(" ")
            ?.filter { it.isNotBlank() }
            ?.associateWith { 1L }
            ?: error("Can't read input")

        var currentGeneration = engravedStonesToCount
        repeat(75) {
            currentGeneration = getNextGeneration(currentGeneration)
        }

        return currentGeneration.values.sum()
    }

}