package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day13.solvePart1()}")
    println("Part 2: ${Day13.solvePart2()}")
}

object Day13 {

    fun solvePart1(): Long {
        val games = Day13::class.java.classLoader.getResource("day13.txt")
            ?.readText()
            ?.split("\r\n\r\n")
            ?.map { Game.fromString(it) }
            ?: error("Can't read input")

        return games.filter { it.isSolvable() }.sumOf { it.getRequiredTokenAmount() }
    }

    fun solvePart2(): Long {
        val games = Day13::class.java.classLoader.getResource("day13.txt")
            ?.readText()
            ?.split("\r\n\r\n")
            ?.map { Game.fromLevel2String(it) }
            ?: error("Can't read input")

        return games.filter { it.isSolvable() }.sumOf { it.getRequiredTokenAmount() }
    }

    data class Game(val buttonA: Button, val buttonB: Button, val price: Position) {

        fun getAcount(): Double {
            return (price.y - buttonB.yModifier * getBcount()) / buttonA.yModifier
        }

        fun getBcount(): Double {
            return (price.x * buttonA.yModifier - price.y * buttonA.xModifier).toDouble() /
                    (buttonB.xModifier * buttonA.yModifier - buttonB.yModifier * buttonA.xModifier)
        }

        fun isSolvable(): Boolean {
            return getAcount() % 1 == 0.0 && getBcount() % 1 == 0.0
        }

        fun getRequiredTokenAmount(): Long {
            return getAcount().toLong() * 3L + getBcount().toLong()
        }

        companion object {
            fun fromString(string: String): Game {
                val (buttonA, buttonB, price) = string.split("\r\n")
                return Game(
                    Button.fromString(buttonA, 3),
                    Button.fromString(buttonB, 1),
                    Position.fromPriceString(price)
                )
            }

            fun fromLevel2String(string: String): Game {
                val (buttonA, buttonB, price) = string.split("\r\n")
                return Game(
                    Button.fromString(buttonA, 3),
                    Button.fromString(buttonB, 1),
                    Position.fromLevel2PriceString(price)
                )
            }
        }
    }

    data class Button(
        val xModifier: Int,
        val yModifier: Int,
        val cost: Int
    ) {
        companion object {
            fun fromString(string: String, cost: Int): Button {
                val xModifier = string.substringAfter("X+").substringBefore(",").toInt()
                val yModifier = string.substringAfter("Y+").toInt()
                return Button(xModifier, yModifier, cost)
            }
        }
    }

    data class Position(
        val x: Long,
        val y: Long
    ) {
        companion object {
            fun fromPriceString(string: String): Position {
                val x = string.substringAfter("X=").substringBefore(",").toLong()
                val y = string.substringAfter("Y=").toLong()
                return Position(x, y)
            }

            fun fromLevel2PriceString(string: String): Position {
                val x = string.substringAfter("X=").substringBefore(",").toInt() + 10000000000000L
                val y = string.substringAfter("Y=").toInt() + 10000000000000L
                return Position(x, y)
            }
        }
    }

}