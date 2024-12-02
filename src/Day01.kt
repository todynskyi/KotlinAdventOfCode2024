import java.util.PriorityQueue
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = toPairs(input)
        val leftList = PriorityQueue(pairs.map { it.first })
        val rightList = PriorityQueue(pairs.map { it.second })

        var sum = 0
        while (leftList.isNotEmpty()) {
            sum += abs(rightList.poll() - leftList.poll())
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        val pairs = toPairs(input)
        val counts = pairs.map { it.second }.groupingBy { it }.eachCount()

        return pairs.sumOf { it.first * counts.getOrDefault(it.first, 0) }
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
