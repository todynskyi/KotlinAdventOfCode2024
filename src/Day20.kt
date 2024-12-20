

fun main() {

    fun List<String>.search(target: Char): Point = this.flatMapIndexed { y, row ->
         row.mapIndexedNotNull  { x, c ->
             if (c == target) Point(x, y) else null
         }
     }.first()

    fun List<String>.toPoints(): List<Point> {
        val end = this.search('E')
        val points = mutableListOf(this.search('S'))
        while (points.last() != end) {
            points.add(points.last().neighbors().filter { this[it.y][it.x] != '#' }.first { it != points.getOrNull(points.lastIndex - 1) })
        }

        return points
    }

    fun List<Point>.findCheats(cheatTime: Int): Int = this.indices.sumOf { start ->
        (start + 100..this.lastIndex).count { end ->
            val distance = this[start].distanceTo(this[end])
            distance <= cheatTime && distance <= end - start - 100
        }
    }

    fun part1(input: List<String>): Int = input.toPoints().findCheats( 2)

    fun part2(input: List<String>): Int = input.toPoints().findCheats(20)

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
