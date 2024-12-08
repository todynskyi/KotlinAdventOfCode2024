fun main() {
    fun List<String>.toGrid(): Collection<List<Point>> = this
        .flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c != '.') c to Point(x, y) else null
            }
        }
        .filterNotNull()
        .groupBy({ it.first }, { it.second })
        .values

    fun List<String>.isMatches(it: Point) =
        it.y in this.indices && it.x in this[it.y].indices

    fun part1(input: List<String>): Int = input.toGrid().flatMap { nodes ->
        nodes.flatMapIndexed { i: Int, a: Point ->
            nodes.drop(i + 1).flatMap { b ->
                val distance = a - b
                if (a.y > b.y) setOf(a - distance, b + distance) else setOf(a + distance, b - distance)
            }
        }.filter { input.isMatches(it) }
    }.toSet().size


    fun part2(input: List<String>): Int = input.toGrid().flatMap { nodes ->
        nodes.flatMapIndexed { i: Int, a: Point ->
            nodes.drop(i + 1).flatMap { b ->
                val distance = a - b
                generateSequence(a) { it - distance }.takeWhile { input.isMatches(it) }.toSet() +
                        generateSequence(a) { it + distance }.takeWhile { input.isMatches(it) }.toSet()
            }
        }.filter { input.isMatches(it) }
    }.toSet().size

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point =
        Point(x + other.x, y + other.y)

    operator fun minus(other: Point): Point =
        Point(x - other.x, y - other.y)
}