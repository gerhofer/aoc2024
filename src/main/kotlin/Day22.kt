package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day22.solvePart1()}")
    println("Part 2: ${Day22.solvePart2()}")
}

object Day22 {

    fun solvePart1(): Long {
        val startingNumbers = Day22::class.java.classLoader.getResource("Day22-test.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.toLong() }
            ?: error("Can't read input")

        val laterPrices = startingNumbers.map { initial ->
            var next = initial
            repeat(2000) {
                next = calculateNext(next)
            }
            next
        }

        return laterPrices.sum()
    }

    fun calculateNext(previous: Long): Long {
        val firstStep = ((previous * 64) xor previous) % 16777216
        val secondStep = ((firstStep / 32) xor firstStep) % 16777216
        return ((secondStep * 2048) xor secondStep) % 16777216
    }

    fun solvePart2(): Int {
        val startingNumbers = Day22::class.java.classLoader.getResource("Day22.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.toLong() }
            ?: error("Can't read input")

        val changeSequencesToAmount = startingNumbers.map { initial ->
            var previous = initial
            val changes = mutableListOf<Int>()
            val amounts = mutableListOf<Int>()
            repeat(2000) {
                val next = calculateNext(previous)
                amounts.add((next % 10).toInt())
                changes.add((next % 10).toInt() - (previous % 10).toInt())
                previous = next
            }
            val changesToBananaCount = mutableMapOf<List<Int>, Int>()
            for (i in 3 until changes.size) {
                val currentSequence = listOf(changes[i - 3], changes[i - 2], changes[i - 1], changes[i])
                if (!changesToBananaCount.contains(currentSequence)) {
                    changesToBananaCount.put(currentSequence, amounts[i])
                }
            }
            changesToBananaCount
        }

        val sequencesToCounts = changeSequencesToAmount.reduce { new, initial ->
            (new.keys + initial.keys)
                .associateWith { new.getOrDefault(it, 0) + initial.getOrDefault(it, 0) }.toMutableMap()
        }
        return sequencesToCounts.maxOf { it.value }
    }

}