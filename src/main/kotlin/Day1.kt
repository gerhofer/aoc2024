package org.example

import kotlin.math.abs

fun main(args: Array<String>) {
    println("Part 1: ${Day1.solvePart1()}")
    println("Part 2: ${Day1.solvePart2()}")
}

object Day1 {

    fun solvePart1(): Int {
        val input = Day1::class.java.classLoader.getResource("day1.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
            ?: error("Can't read input")

        val firstList = input.map { it.first() }.sorted()
        val secondList = input.map { it.last() }.sorted()

        return firstList.zip(secondList)
            .sumOf { (first, second) -> abs(first - second) }
    }

    fun solvePart2(): Int {
        val input = Day1::class.java.classLoader.getResource("day1.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { it.split(" ").filter { it.isNotBlank() }.map { it.toInt() } }
            ?: error("Can't read input")

        val firstList = input.map { it.first() }
        val secondListByNumber = input.map { it.last() }.groupBy { it }

        return firstList.sumOf { it * secondListByNumber.getOrDefault(it, emptyList()).size }
    }

}