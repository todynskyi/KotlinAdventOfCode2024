fun main() {
    val directions = listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1
    )

    val targetXMAS = "XMAS"

    fun hasXMASWord(input: List<String>, row: Int, col: Int, direction: Pair<Int, Int>): Boolean {
        if (row + (targetXMAS.length - 1) * direction.first !in input.indices || col + (targetXMAS.length - 1) * direction.second !in input[0].indices) {
            return false
        }
        var word = ""
        for (i in targetXMAS.indices) {
            word += input[row + i * direction.first][col + i * direction.second]
        }
        return word == targetXMAS
    }

    fun part1(input: List<String>): Int {
        return input.flatMapIndexed { i, row ->
            row.mapIndexed { j, c ->
                if (c == 'X') {
                    directions.count { hasXMASWord(input, i, j, it) }
                } else 0
            }
        }.sum()
    }

    val targetMAS = "MAS"
    val targetMASReverse = targetMAS.reversed()

    fun hasMASWord(input: List<String>, row: Int, col: Int): Boolean {
        if (row + 2 >= input.size || col + 2 >= input[0].length) {
            return false
        }

        val topLeft = input[row][col]
        val topRight = input[row][col + 2]
        val middle = input[row + 1][col + 1]
        val bottomLeft = input[row + 2][col]
        val bottomRight = input[row + 2][col + 2]

        val diagonal1 = listOf(topLeft, middle, bottomRight).joinToString("")
        val diagonal2 = listOf(topRight, middle, bottomLeft).joinToString("")

        return (diagonal1 == targetMAS || diagonal1 == targetMASReverse) && (diagonal2 == targetMAS || diagonal2 == targetMASReverse)
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (row in 0..input.size - 3) {
            for (col in 0..input[0].length - 3) {
                count += if (hasMASWord(input, row, col)) 1 else 0
            }
        }
        return count
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
