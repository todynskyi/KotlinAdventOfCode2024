
fun main() {

    val keypad = listOf("789", "456", "123", " 0A", " ^@", "<v>")
        .flatMapIndexed { rowIndex: Int, row: String ->
            row.mapIndexed { colIndex, c ->
                Point(rowIndex, colIndex) to c
            }
        }
        .filter { it.second != ' ' }
        .toMap()
        .toMutableMap()
        .toList()
        .associate { it.second to it.first }

    val cache = mutableMapOf<Triple<String, Int, Int>, Long>()

    fun <T> List<T>.toPermutations(): List<List<T>> {
        return if (this.size == 1) listOf(this)
        else this.flatMap { i -> (this - i).toPermutations().map { listOf(i) + it } }
    }

    fun String.toPermutations() = this.toList().toPermutations().map { it.joinToString("") }

    fun Char.toPoint(): Point {
        return when (this.uppercaseChar()) {
            'N', 'U', '^', '3' -> Point(-1, 0)
            'E', 'R', '>', '0' -> Point(0, 1)
            'S', 'D', 'V', '1' -> Point(1, 0)
            'W', 'L', '<', '2' -> Point(0, -1)
            else -> error("Invalid char: $this")
        }
    }

    fun calculate(sequence: String, limit: Int, depth: Int): Long {
        val key = Triple(sequence, depth, limit)
        return cache.getOrPut(key) {
            sequence.fold(Pair(if (depth == 0) keypad['A']!! else keypad['@']!!, 0L)) { (pos, sum), char ->
                val next = keypad[char]!!
                val (dx, dy) = next - pos
                val paths = ((if (dx < 0) "^".repeat(-dx) else "v".repeat(dx)) + if (dy < 0) "<".repeat(-dy) else ">".repeat(dy)).toPermutations()
                    .filter { path -> path.asSequence().runningFold(pos) { pos, dir -> pos + dir.toPoint() }.all { it in keypad.values } }
                    .map { "$it@" }.ifEmpty { listOf("@") }
                next to (sum + if (depth == limit) paths.minOf { it.length }.toLong() else paths.minOfOrNull { calculate(it, limit, depth + 1) } ?: paths.minOf { it.length }.toLong())
            }.second
        }
    }

    fun List<String>.solve(limit: Int): Long {
        return this.sumOf { code -> calculate(code, limit, 0) * code.filter { it.isDigit() }.toLong() }
    }

    fun part1(input: List<String>): Long = input.solve(2)

    fun part2(input: List<String>): Long = input.solve(25)

    val testInput = readInput("Day21_test")
    check(part1(testInput) == 126384L)

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}