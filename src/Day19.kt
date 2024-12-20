

fun main() {

    fun calculateDesigns(towel: String, patterns: List<String>, cache : MutableMap<String, Long>): Long {
        return cache.getOrPut(towel) {
            if (towel.isEmpty()) {
                1
            } else {
                patterns.filter { it in towel }
                    .sumOf {
                        if (towel.startsWith(it)) {
                            calculateDesigns(towel.removePrefix(it), patterns, cache)
                        } else {
                            0
                        }
                    }
            }
        }
    }

    fun List<String>.solve(): List<Long> {
        val patterns = this[0].split(", ")
        val design = this.drop(2)

        val cache = mutableMapOf<String, Long>()

        return design.map { calculateDesigns(it, patterns, cache) }
    }

    fun part1(input: List<String>): Int = input.solve().count { it > 0 }

    fun part2(input: List<String>): Long = input.solve().sum()

    val testInput = readInput("Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
