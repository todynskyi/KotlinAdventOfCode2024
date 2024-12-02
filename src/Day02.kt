fun main() {
    fun List<Int>.isSafeReport(): Boolean {
        val isIncreasing = this[0] < this[1]
        return this.windowed(2).none { (prev, current) ->
            (isIncreasing && ((current > prev && current - prev > 3) || prev >= current)) ||
                    (!isIncreasing && ((prev > current && prev - current > 3) || current >= prev))
        }
    }

    fun part1(input: List<String>): Int {
        val lists = input.toLists()
        return lists.count { it.isSafeReport() }
    }

    fun List<Int>.isAnySafeReport(): Boolean {
        for (i in this.indices) {
            if (this.toMutableList()
                    .apply { this.removeAt(i) }
                    .isSafeReport()
            ) {
                return true
            }
        }
        return false
    }

    fun part2(input: List<String>): Int {
        val lists = input.toLists()
        return lists.count {
            val isSafe = it.isSafeReport()
            if (isSafe) true else it.isAnySafeReport()
        }
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
