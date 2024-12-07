fun main() {
    val add: (Long, Long) -> Long = { a, b -> a + b }
    val multiply: (Long, Long) -> Long = { a, b -> a * b }
    val concat: (Long, Long) -> Long = { a, b -> "$a$b".toLong() }

    fun isCorrect(
        target: Long,
        operators: List<(Long, Long) -> Long>,
        elements: List<Long>,
        sum: Long,
        index: Int
    ): Boolean {
        if (index >= elements.size) {
            return sum == target
        }

        return operators.any { operator ->
            val next = elements[index]
            isCorrect(target, operators, elements, operator(sum, next), index + 1)
        }
    }

    fun sum(input: List<String>, operators: List<(Long, Long) -> Long>): Long {
        return input
            .map {
                val solution = it.substringBefore(":").toLong()
                val elements = it.substringAfter(": ").split(" ").map { num -> num.toLong() }
                solution to elements
            }
            .filter { (solution, elements) ->
                isCorrect(
                    solution,
                    operators,
                    elements,
                    elements.first(),
                    1
                )
            }
            .sumOf { it.first }
    }

    fun part1(input: List<String>): Long {
        return sum(input, listOf(add, multiply))
    }

    fun part2(input: List<String>): Long {
        return sum(input, listOf(add, multiply, concat))
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
