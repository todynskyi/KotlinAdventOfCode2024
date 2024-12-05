fun main() {

    fun sum(input: List<String>, predicate: (Pair<List<String>, List<String>>) -> Boolean): Int {
        val orderingRules = input.takeWhile { it.isNotEmpty() }.toSet()
        return input.asSequence()
            .dropWhile { it.isNotBlank() }
            .drop(1)
            .map { it.split(",") }
            .map {
                it to it.sortedWith { o1, o2 ->
                    when {
                        "$o1|$o2" in orderingRules -> -1
                        "$o2|$o1" in orderingRules -> 1
                        else -> 0
                    }
                }
            }
            .filter(predicate)
            .sumOf { it.second[it.second.lastIndex / 2].toInt() }
    }

    fun part1(input: List<String>): Int {
        return sum(input) {
            it.first == it.second
        }
    }

    fun part2(input: List<String>): Int {
        return sum(input) {
            it.first != it.second
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
