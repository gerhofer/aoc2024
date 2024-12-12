package org.example

fun main(args: Array<String>) {
    println("Part 1: ${Day12.solvePart1()}")
    println("Part 2: ${Day12.solvePart2()}")
}

object Day12 {

    fun solvePart1(): Long {
        val plotFields = Day12::class.java.classLoader.getResource("day12.txt")
            ?.readText()
            ?.split("\r\n")
            ?.mapIndexed { y, line ->
                line.split("").filter { it.isNotBlank() }
                    .mapIndexed { x, value -> GardenPlot(value, Coordinate(x, y)) }
            }
            ?: error("Can't read input")
        val shapes = groupShapes(plotFields)
        return shapes.sumOf { it.getArea() * it.getFence() }
    }

    private fun groupShapes(plotFields: List<List<GardenPlot>>): MutableSet<Shape> {
        val shapes = mutableSetOf<Shape>()
        val gardenPlotsToGroup = plotFields.flatten().toMutableSet()
            .associateBy { it.coordinate }
            .toMutableMap()
        while (gardenPlotsToGroup.isNotEmpty()) {
            val shape = getConnectedShape(gardenPlotsToGroup, gardenPlotsToGroup.values.first())
            shapes.add(shape)
            shape.coordinates.forEach { alreadyMatched -> gardenPlotsToGroup.remove(alreadyMatched) }
        }
        return shapes
    }

    val NEVER_MATCHING = GardenPlot("NEVER", Coordinate(-1, -1))

    fun getConnectedShape(plotFields: Map<Coordinate, GardenPlot>, currentStart: GardenPlot): Shape {
        val currentlyConnected = mutableSetOf(currentStart)
        addNeighbours(plotFields, currentStart, currentlyConnected)
        return Shape(currentStart.letter, currentlyConnected.map { it.coordinate })
    }

    fun addNeighbours(
        plotFields: Map<Coordinate, GardenPlot>,
        currentStart: GardenPlot,
        connected: MutableSet<GardenPlot>
    ) {
        currentStart.coordinate.getNeighbours()
            .map { coordinate -> plotFields.getOrDefault(coordinate, NEVER_MATCHING) }
            .filter { neighbour -> neighbour.letter == currentStart.letter }
            .forEach { matchingNeighbour ->
                if (!connected.contains(matchingNeighbour)) {
                    connected.add(matchingNeighbour)
                    addNeighbours(plotFields, matchingNeighbour, connected)
                }
            }
    }

    fun solvePart2(): Long {
        val plotFields = Day12::class.java.classLoader.getResource("day12.txt")
            ?.readText()
            ?.split("\r\n")
            ?.mapIndexed { y, line ->
                line.split("").filter { it.isNotBlank() }
                    .mapIndexed { x, value -> GardenPlot(value, Coordinate(x, y)) }
            }
            ?: error("Can't read input")
        val shapes = groupShapes(plotFields)
        return shapes.sumOf { it.getArea() * it.getNumberOfSides() }
    }

    data class Coordinate(val x: Int, val y: Int) {

        fun getAbove(): Coordinate = Coordinate(x, y - 1)
        fun getBelow(): Coordinate = Coordinate(x, y + 1)
        fun getLeft(): Coordinate = Coordinate(x - 1, y)
        fun getRight(): Coordinate = Coordinate(x + 1, y)
        fun getNeighbours(): List<Coordinate> = listOf(getLeft(), getRight(), getAbove(), getBelow())

    }

    data class GardenPlot(val letter: String, val coordinate: Coordinate)

    data class Shape(val letter: String, val coordinates: List<Coordinate>) {

        fun getArea(): Long = coordinates.size.toLong()

        fun getNumberOfSides(): Long {
            val minX = coordinates.minOf { it.x }
            val maxX = coordinates.maxOf { it.x }
            val minY = coordinates.minOf { it.y }
            val maxY = coordinates.maxOf { it.y }

            val alreadySeenFences = listOf("LEFT", "RIGHT", "ABOVE", "BELOW")
                .associateWith { mutableSetOf<Coordinate>() }
            var numberOfSides = 0L
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val current = Coordinate(x, y)
                    if (coordinates.contains(current)) {
                        val left = current.getLeft()
                        if (!coordinates.contains(left)) {
                            if (!alreadySeenFences["LEFT"]!!.contains(left.getAbove()) && !alreadySeenFences["LEFT"]!!.contains(left.getBelow())) {
                                numberOfSides++
                            }
                            alreadySeenFences["LEFT"]!!.add(left)
                        }

                        val right = current.getRight()
                        if (!coordinates.contains(right)) {
                            if (!alreadySeenFences["RIGHT"]!!.contains(right.getAbove()) && !alreadySeenFences["RIGHT"]!!.contains(right.getBelow())) {
                                numberOfSides++
                            }
                            alreadySeenFences["RIGHT"]!!.add(right)
                        }

                        val above = current.getAbove()
                        if (!coordinates.contains(above)) {
                            if (!alreadySeenFences["ABOVE"]!!.contains(above.getLeft()) && !alreadySeenFences["ABOVE"]!!.contains(above.getRight())) {
                                numberOfSides++
                            }
                            alreadySeenFences["ABOVE"]!!.add(above)
                        }

                        val below = current.getBelow()
                        if (!coordinates.contains(below)) {
                            if (!alreadySeenFences["BELOW"]!!.contains(below.getLeft()) && !alreadySeenFences["BELOW"]!!.contains(below.getRight())) {
                                numberOfSides++
                            }
                            alreadySeenFences["BELOW"]!!.add(below)
                        }
                    }
                }
            }

            return numberOfSides
        }

        fun getFence(): Long {
            val minX = coordinates.minOf { it.x }
            val maxX = coordinates.maxOf { it.x }
            val minY = coordinates.minOf { it.y }
            val maxY = coordinates.maxOf { it.y }

            var fence = 0L
            for (y in minY..maxY) {
                for (x in minX..maxX) {
                    val current = Coordinate(x, y)
                    if (coordinates.contains(current)) {
                        val notIncluded = current.getNeighbours().count { !coordinates.contains(it) }
                        fence += notIncluded
                    }
                }
            }
            return fence
        }
    }

}