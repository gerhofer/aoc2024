package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day2.solvePart1()}")
    println("Part 2: ${Day2.solvePart2()}")
}

object Day2 {

    fun solvePart1(): Int {
        val reports = Day1::class.java.classLoader.getResource("day2.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
            ?: error("Can't read input")

        return reports.count { isSafe(it) }
    }

    private fun isSafe(report: List<Int>): Boolean {
        return report.windowed(2).all { (first, second) ->
            first > second && first - second in (1..3)
        } || report.windowed(2).all { (first, second) ->
            second > first && second - first in (1..3)
        }
    }

    fun solvePart2(): Int {
        val reports = Day1::class.java.classLoader.getResource("day2.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
            ?: error("Can't read input")

        return reports.count {
            getPermutations(it).any { option -> isSafe(option) }
        }
    }

    private fun getPermutations(original: List<Int>): List<List<Int>> {
        val options = mutableListOf(original)
        for (i in original.indices) {
            options.add(
                original.subList(0, i) + if (i < original.lastIndex) {
                    original.subList(i + 1, original.size)
                } else {
                    emptyList()
                }
            )
        }
        return options
    }

}