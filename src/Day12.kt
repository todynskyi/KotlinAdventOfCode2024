fun main() {
    operator fun List<String>.get(at: Point): Char? =
        if (at.y in this.indices && at.x in this[at.y].indices) this[at.y][at.x] else null

    fun Point.count(input: List<String>): Int =
        listOf(
            Point(0, -1),
            Point(1, 0),
            Point(0, 1),
            Point(-1, 0),
            Point(0, -1)
        ).zipWithNext()
            .map { (first, second) ->
                listOf(
                    input[this],
                    input[this + first],
                    input[this + second],
                    input[this + first + second]
                )
            }.count { (target, side1, side2, corner) ->
                (target != side1 && target != side2) || (side1 == target && side2 == target && corner != target)
            }

    fun List<String>.findRegion(start: Point, seen: MutableSet<Point>): Region {
        val target: Char = this[start]!!
        val queue = mutableListOf(start)
        var area = 0
        var perimeter = 0
        var sides = 0

        while (queue.isNotEmpty()) {
            val place = queue.removeFirst()
            if (this[place] == target && place !in seen) {
                seen += place
                area++
                val neighbors = place.neighbors()
                queue.addAll(neighbors)
                perimeter += neighbors.count { this[it] != target }
                sides += place.count(this)
            }
        }
        return Region(target, area, perimeter, sides)
    }


    fun List<String>.findRegions(selector: (Region) -> Int): Int {
        val seen = mutableSetOf<Point>()
        return this.flatMapIndexed { y, row ->
            row.mapIndexedNotNull { x, _ ->
                Point(x, y).let { if (it !in seen) findRegion(it, seen) else null }
            }
        }.sumOf(selector)
    }


    fun part1(input: List<String>): Int {
        return input.findRegions { it.area * it.perimeter }
    }

    fun part2(input: List<String>): Int {
        return input.findRegions { it.area * it.sides }
    }

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

data class Region(val name: Char, val area: Int, val perimeter: Int, val sides: Int)