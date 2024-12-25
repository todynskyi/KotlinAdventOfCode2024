
fun main() {

    fun List<String>.toSchematics(): Pair<List<List<Int>>, List<List<Int>>> {
        val keys = mutableListOf<List<Int>>()
        val locks = mutableListOf<List<Int>>()
        this.chunked(8).map { pattern ->
            val pins = (0..4).map { col ->
                (0..6).count { row ->
                    pattern[row][col] == '#'
                } - 1
            }
            if (pattern[0][0] == '#') locks += pins else keys += pins
        }
        return keys to locks
    }

    fun part1(input: List<String>): Int = input.toSchematics().let { (keys, locks) ->
        keys.sumOf { key ->
            locks.count { lock ->
                key.zip(lock).all { (k, l) ->
                    k + l <= 5
                }
            }
        }
    }

    val testInput = readInput("Day25_test")
    check(part1(testInput) == 3)
    val input = readInput("Day25")
    part1(input).println()
}
