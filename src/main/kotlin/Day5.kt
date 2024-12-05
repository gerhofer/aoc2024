package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day5.solvePart1()}")
    println("Part 2: ${Day5.solvePart2()}")
}

object Day5 {

    fun solvePart1(): Int {
        val (rulesAsString, updatesAsString) = Day5::class.java.classLoader.getResource("day5.txt")
            ?.readText()
            ?.split("\r\n\r\n")
            ?: error("Can't read input")
        val rules = parseRules(rulesAsString)
        val options = parseUpdates(updatesAsString)

        return options.filter {
            isValid(it, rules)
        }.sumOf {
            it[it.size / 2]
        }
    }

    fun solvePart2(): Int {
        val (rulesAsString, updatesAsString) = Day5::class.java.classLoader.getResource("day5.txt")
            ?.readText()
            ?.split("\r\n\r\n")
            ?: error("Can't read input")

        val rules = parseRules(rulesAsString)
        val options = parseUpdates(updatesAsString)

        return options.filter {
            !isValid(it, rules)
        }.map {
            order(it, rules)
        }.sumOf {
            it[it.size / 2]
        }
    }

    private fun isValid(update: List<Int>, rules: Map<Int, List<Int>>): Boolean {
        for (current in update.indices) {
            for (after in current..<update.size) {
                val first = update[current]
                val other = update[after]
                if (rules.getOrDefault(other, emptyList()).contains(first)) {
                    return false
                }
            }
        }
        return true
    }

    private fun order(update: List<Int>, rules: Map<Int, List<Int>>): List<Int> {
        val needingOrdering = update.toMutableList()
        val orderedList = mutableListOf<Int>()
        while (needingOrdering.isNotEmpty()) {
            val next = findLast(needingOrdering, rules)
            needingOrdering.remove(next)
            orderedList.addFirst(next)
        }
        return orderedList
    }

    private fun findLast(update: List<Int>, rules: Map<Int, List<Int>>): Int {
        val valueToNeedingToBeAfter = update.associateWith { rules.getOrDefault(it, emptyList()) }
        val valueToAllOthers = update.associateWith { update.filter { o -> o != it } }

        return update.first {
            val others = valueToAllOthers[it]?: emptyList()
            val needIngToBeAfter = valueToNeedingToBeAfter[it] ?: emptyList()
            needIngToBeAfter.intersect(others).isEmpty()
        }
    }

    private fun parseRules(rules: String): Map<Int, List<Int>> {
        return rules.split("\r\n")
            .map { line ->
                val ruleNumbers = line.split("|")
                    .filter { it.isNotBlank() }
                    .map { it.toInt() }
                ruleNumbers.first() to ruleNumbers.last()
            }.groupBy { pair -> pair.first }
            .mapValues { groupedPairs -> groupedPairs.value.map { pair -> pair.second } }
    }

    private fun parseUpdates(options: String): List<List<Int>> {
        return options.split("\r\n")
            .map { line ->
                line.split(",").map { it.toInt() }
            }
    }
}