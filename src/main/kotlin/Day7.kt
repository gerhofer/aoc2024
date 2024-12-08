package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day7.solvePart1()}")
    println("Part 2: ${Day7.solvePart2()}")
}

object Day7 {

    fun solvePart1(): Long {
        val calibrations = Day7::class.java.classLoader.getResource("day7-test.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { Calibration.parse(it) }
            ?: error("Can't read input")

        return calibrations
            .filter { it.couldBeMathNoConcat() }
            .sumOf { it.result }
    }

    fun solvePart2(): Long {
        val calibrations = Day7::class.java.classLoader.getResource("day7.txt")
            ?.readText()
            ?.split("\r\n")
            ?.map { Calibration.parse(it) }
            ?: error("Can't read input")

        return calibrations
            .filter { it.couldBeMath() }
            .sumOf { it.result }
    }

    data class Calibration(
        val result: Long,
        val numbers: List<Long>
    ) {

        fun couldBeMathNoConcat(): Boolean {
            val operatorOptions = getOperatorOptionsNoConcat(numbers.size - 1, (1..numbers.size - 1).map { emptyList() }).distinct()
            return couldBeMath(operatorOptions)
        }

        fun couldBeMath(): Boolean {
            val operatorOptions = getOperatorOptions(numbers.size - 1, (1..numbers.size - 1).map { emptyList() }).distinct()
            return couldBeMath(operatorOptions)
        }

        private fun couldBeMath(operatorOptions: List<List<Operator>>): Boolean {
            for (option in operatorOptions) {
                var previous = numbers.first()
                for (index in option.indices) {
                    val next = numbers[index + 1]
                    val operator = option[index]
                    when (operator) {
                        Operator.ADD -> previous += next
                        Operator.MULTIPLY -> previous *= next
                        Operator.CONCAT -> previous = (previous.toString() + next.toString()).toLong()
                    }
                }
                if (previous == result) {
                    return true
                }
            }
            return false
        }

        private fun getOperatorOptionsNoConcat(size: Int, current: List<List<Operator>>): List<List<Operator>> {
            if (size == 0) {
                return current
            }
            val newList = current.map { it + listOf(Operator.MULTIPLY) } +
                    current.map { it + listOf(Operator.ADD) }
            return getOperatorOptionsNoConcat(size - 1, newList)
        }

        private fun getOperatorOptions(size: Int, current: List<List<Operator>>): List<List<Operator>> {
            if (size == 0) {
                return current
            }
            val newList = current.map { it + listOf(Operator.MULTIPLY) } +
                    current.map { it + listOf(Operator.ADD) } +
                    current.map { it + listOf(Operator.CONCAT) }
            return getOperatorOptions(size - 1, newList)
        }

        enum class Operator {
            ADD, MULTIPLY, CONCAT
        }

        companion object {
            fun parse(value: String): Calibration {
                val (calibrationResult, numbers) = value.split(":")
                return Calibration(
                    calibrationResult.toLong(),
                    numbers.trim().split(" ").map { it.toLong() }
                )
            }
        }
    }

}