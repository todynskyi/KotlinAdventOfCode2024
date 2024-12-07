fun main() {
    fun List<String>.toGrid(): Map<Pair<Int, Int>, Char> =
        this.mapIndexed { row, line -> line.mapIndexed { col, symbol -> (row to col) to symbol } }
            .flatten()
            .toMap()

    val directions = mapOf(
        '^' to '>',
        '>' to 'v',
        'v' to '<',
        '<' to '^',
    )

    fun next(direction: Char, position: Pair<Int, Int>): Pair<Int, Int> {
        val (row, col) = position

        return when (direction) {
            '^' -> (row - 1) to col
            '>' -> row to col + 1
            'v' -> (row + 1) to col
            '<' -> row to col - 1
            else -> error("unknown direction: $direction")
        }
    }


    fun Map<Pair<Int, Int>, Char>.visit(obstructions: Pair<Int, Int>? = null): Pair<Set<Pair<Int, Int>>, Boolean> {
        var (position, direction) = this.entries.first { it.value == '^' }
        val visited = mutableSetOf<Pair<Pair<Int, Int>, Char>>()

        var alreadyVisited = false
        while (true) {
            if (visited.contains(position to direction)) {
                alreadyVisited = true
                break
            }

            visited.add(position to direction)
            val target = next(direction, position)

            when {
                this[target] == '#' || target == obstructions -> direction =
                    directions[direction] ?: error("unknown direction $direction")

                this[target] == null -> break
                else -> position = target
            }
        }

        val visitedPositions = visited.map { (positions) -> positions }.toSet()
        return visitedPositions to alreadyVisited
    }


    fun part1(input: List<String>): Int = input.toGrid().visit().first.size

    fun part2(input: List<String>): Int = input.toGrid().visit()
        .let { visitedPositions -> visitedPositions.first.count { input.toGrid().visit(it).second } }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}