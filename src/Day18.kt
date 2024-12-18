
fun main() {

    fun String.toPoint() = Point(this.substringBefore(",").toInt(), this.substringAfter(",").toInt())

    fun List<String>.toPoints(): Set<Point> = this.take(1024)
        .map { it.toPoint() }
        .toSet()

    fun Set<Point>.solve(): Int? {
        val maxX = 70
        val maxY = 70
        val end = Point(maxX, maxY)
        val queue = ArrayDeque<Pair<Point, Int>>()
        queue.add(Point(0, 0) to 0)
        val seen = mutableSetOf<Point>()

        while (queue.isNotEmpty()) {
            val (current, cost) = queue.removeFirst()

            if (!seen.add(current)) continue
            if (current == end) return cost

            queue.addAll(current.neighbors()
                .filter { it.x in 0..maxX && it.y in 0..maxY }
                .filter { it !in this }
                .map { it to cost + 1 }
            )
        }

        return null
    }

    fun part1(input: List<String>): Int = input.toPoints().solve()!!

    fun part2(input: List<String>): String {
        val points = input.map { it.toPoint() }
        var max = input.size
        var min = 1024
        while (min < max) {
            val mid = (max + min) / 2
            if (points.take(mid).toSet().solve() == null) max = mid else min = mid + 1
        }

        return points[min - 1].let { "${it.x},${it.y}" }
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
