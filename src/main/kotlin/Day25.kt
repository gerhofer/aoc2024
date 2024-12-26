package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day25.solvePart1()}")
}

object Day25 {

    fun solvePart1(): Int {
        val schematics = Day25::class.java.classLoader.getResource("Day25.txt")
            ?.readText()
            ?.split("\r\n\r\n")
            ?.map { it.split("\r\n") }
            ?: error("Can't read input")

        val keys = schematics.filter { Key.isKey(it) }
            .map { Key.parse(it) }
        val locks = schematics.filter { Lock.isLock(it) }
            .map { Lock.parse(it) }

        return keys.sumOf { key -> key.countOpens(locks) }
    }

    data class Key(val pins: List<Int>) {

        fun countOpens(locks: List<Lock>): Int {
            return locks.count { lock -> this.canOpen(lock) }
        }

        fun canOpen(lock: Lock): Boolean {
            return this.pins.zip(lock.pins).all {
                (keyPin, lockPin) -> (keyPin + lockPin) < 6
            }
        }

        companion object {
            fun isKey(schematic: List<String>): Boolean {
                return schematic.last().all { it == '#' }
            }

            fun parse(schematic: List<String>): Key {
                return Key(countPins(schematic))
            }
        }
    }

    private fun countPins(schematic: List<String>) = schematic.first().indices.map { col ->
        schematic.map { it[col] }.count { letter -> letter == '#' } - 1
    }

    data class Lock(val pins: List<Int>) {

        companion object {
            fun isLock(schematic: List<String>): Boolean {
                return schematic.first().all { it == '#' }
            }

            fun parse(schematic: List<String>): Lock {
                return Lock(countPins(schematic))
            }
        }
    }

}