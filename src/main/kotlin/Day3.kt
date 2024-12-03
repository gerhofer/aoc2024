package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day3.solvePart1()}")
    println("Part 2: ${Day3.solvePart2()}")
}

object Day3 {

    private val MUL_REGEX = Regex("mul\\(([\\d]*),([\\d]*)\\)")

    fun solvePart1(): Long {
        val plainInput = Day3::class.java.classLoader.getResource("day3.txt")
            ?.readText()
            ?.split("\r\n")
            ?.joinToString("")
            ?: error("Can't read input")

        return MUL_REGEX.findAll(plainInput).sumOf {
            it.groupValues[1].toLong() * it.groupValues.last().toLong()
        }
    }

    fun solvePart2(): Long {
        val plainInput = Day3::class.java.classLoader.getResource("day3.txt")
            ?.readText()
            ?.split("\r\n")
            ?.joinToString("")
            ?: error("Can't read input")

        var sum = 0L
        var multiplicationsAreEnabled = true
        for (i in plainInput.indices) {
            var currentSubstring = plainInput.substring(i)
            if (currentSubstring.startsWith("don't()")) {
                multiplicationsAreEnabled = false
            } else if (currentSubstring.startsWith("do()")) {
                multiplicationsAreEnabled = true
            } else if (multiplicationsAreEnabled) {
                val result = MUL_REGEX.find(currentSubstring)
                if (result != null && result.range.start == 0) {
                    val mathResult = result.groupValues[1].toLong() * result.groupValues.last().toLong()
                    sum += mathResult
                }
            }
        }

        return sum
    }

}