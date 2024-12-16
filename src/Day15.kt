fun main() {

    fun List<String>.toWarehouses(): List<CharArray> = this.takeWhile { it.isNotEmpty() }
        .map { it.toCharArray() }

    fun List<String>.toMovements(): List<Point> = this.dropWhile { it.isNotEmpty() }
        .dropWhile { it.isEmpty() }
        .flatMap { row ->
            row.map {
                when (it) {
                    '^' -> Point(0, -1)
                    '>' -> Point(1, 0)
                    'v' -> Point(0, 1)
                    '<' -> Point(-1, 0)
                    else -> error("Invalid direction: $it")
                }
            }
        }

    fun List<CharArray>.findAll(search: Char): List<Point> = this
        .flatMapIndexed { y, row ->
            row.mapIndexed { x, c ->
                if (c == search) Point(x, y) else null
            }
        }
        .filterNotNull()

    fun List<CharArray>.push(position: Point, direction: Point): List<Pair<Point, Point>>? {
        val pushes = mutableListOf<Pair<Point, Point>>()
        val seen = mutableSetOf<Point>()
        val queue = mutableListOf(position)
        while (queue.isNotEmpty()) {
            val point = queue.removeFirst()
            if (point !in seen) {
                seen += point
                if (direction in setOf(Point(0, -1), Point(0, 1))) {
                    when (this[point.y][point.x]) {
                        ']' -> queue.add(point + Point(-1, 0))
                        '[' -> queue.add(point + Point(1, 0))
                    }
                }
                val next = point + direction
                when (this[next.y][next.x]) {
                    '#' -> return null
                    in "[O]" -> queue.add(next)
                }
                pushes.add(point to next)
            }
        }
        return pushes.reversed()
    }

    fun List<CharArray>.move(movements: List<Point>): List<CharArray> {
        val start = this.findAll('@').first()
        var place = start
        movements.forEach { direction ->
            val next = place + direction
            when (this[next.y][next.x]) {
                in "[O]" -> {
                    push(next, direction)?.let { moves ->
                        moves.forEach { (from, to) ->
                            this[to.y][to.x] = this[from.y][from.x]
                            this[from.y][from.x] = '.'
                        }
                        place = next
                    }
                }

                !in "#" -> {
                    place = next
                }
            }
        }
        return this
    }

    fun part1(input: List<String>): Int = input.toWarehouses()
        .move(input.toMovements())
        .findAll('O')
        .sumOf { (100 * it.y) + it.x }

    fun part2(input: List<String>): Int {
        return input.toWarehouses()
            .map { row ->
                row.joinToString("") {
                    when (it) {
                        '#' -> "##"
                        'O' -> "[]"
                        '.' -> ".."
                        '@' -> "@."
                        else -> error("Invalid $it")
                    }
                }.toCharArray()
            }
            .move(input.toMovements())
            .findAll('[')
            .sumOf { (100 * it.y) + it.x }
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 10092)
    check(part2(testInput) == 9021)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
