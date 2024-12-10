fun main() {
    fun List<String>.toGrid(): List<IntArray> = this.map { row ->
        row.map { it.digitToInt() }.toIntArray()
    }

    fun calculateScore(grid: List<IntArray>, point: Point): Int {
        val queue = mutableListOf(point)
        val visited = mutableSetOf<Point>()

        var found = 0
        while (queue.isNotEmpty()) {
            val place = queue.removeFirst()
            if (place !in visited) {
                visited += place
                val height = grid[place.y][place.x]
                if (height == 9) {
                    found++;
                } else {
                    queue.addAll(
                        place.neighbors()
                            .filter { it.y in grid.indices && it.x in grid[it.y].indices }
                            .filter { grid[it.y][it.x] == height + 1 }
                    )
                }
            }
        }
        return found
    }

    fun calculateRatingScore(grid: List<IntArray>, point: Point): Int {
        val queue = mutableListOf(point)

        var found = 0
        while (queue.isNotEmpty()) {
            val place = queue.removeFirst()
            val height = grid[place.y][place.x]
            if (height == 9) {
                found++;
            } else {
                queue.addAll(
                    place.neighbors()
                        .filter { it.y in grid.indices && it.x in grid[it.y].indices }
                        .filter { grid[it.y][it.x] == height + 1 }
                )
            }
        }
        return found
    }

    fun List<String>.calculate(calc: (List<IntArray>, Point) -> Int): Int {
        val grid = this.toGrid()
        return grid.mapIndexed { y, row ->
            row.mapIndexed { x, i ->
                if (i == 0) calc(grid, Point(x, y)) else 0
            }.sum()
        }.sum()
    }

    fun part1(input: List<String>): Int = input.calculate({ grid, point -> calculateScore(grid, point) })

    fun part2(input: List<String>): Int = input.calculate({ grid, point -> calculateRatingScore(grid, point) })

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}

