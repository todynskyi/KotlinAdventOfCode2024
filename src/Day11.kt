fun main() {
    fun List<String>.toStones(): List<Long> {
        return this.first()
            .split(" ")
            .map { it.toLong() }
    }

    fun applyRule(num: Long): List<Long> {
        val numStr = num.toString()
        val n = numStr.length
        return if (n % 2 == 0) {
            val half = n / 2
            listOf(numStr.take(half).toLong(), numStr.drop(half).toLong())
        }
        else if (num == 0L) listOf(1)
        else listOf(num * 2024)
    }

    fun calculate(stones: List<Long>, numberOfBlinking: Int): Int {
        if (numberOfBlinking > 0) {
            return calculate(stones.flatMap { applyRule(it) }, numberOfBlinking - 1)
        }
        return stones.size;
    }

    fun part1(stones: List<Long>): Int {
        return calculate(stones, 25)
    }

    fun part2(stones: List<Long>): Long {
        var counts: Map<Long, Long> = stones.groupingBy { it }
            .eachCount()
            .mapValues { it.value.toLong() }

        repeat(75) {
            val newCounts = mutableMapOf<Long, Long>()
            counts.forEach { (num, count) ->
                applyRule(num).forEach { newNum ->
                    val current = newCounts[newNum] ?: 0
                    newCounts[newNum] = current + count
                }
            }
            counts = newCounts
        }

        return counts.values.sum()
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput.toStones()) == 55312)

    val input = readInput("Day11")
    part1(input.toStones()).println()
    part2(input.toStones()).println()
}
