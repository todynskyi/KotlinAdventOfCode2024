

fun main() {

    fun getSecret(n: Long): Long {
        var value = n
        value = (value * 64).xor(value) % 16777216
        value = (value / 32).xor(value) % 16777216
        value = (value * 2048).xor(value) % 16777216
        return value
    }

    fun part1(input: List<String>): Long = input.sumOf { line ->
        (0..<2000).fold(line.toLong()) { acc, _ -> getSecret(acc) }
    }

    fun part2(input: List<String>): Int = mutableMapOf<List<Int>, Int>().withDefault { 0 }.let { sequences ->
         input.forEach { line ->
             var current = line.toLong()
             val seen = mutableSetOf<List<Int>>()
             (0..<2000)
                 .asSequence()
                 .map { getSecret(current).also { current = it } }
                 .map { (it % 10).toInt() }
                 .windowed(5)
                 .forEach { prices ->
                     val sequence = prices
                         .zipWithNext()
                         .map { it.second - it.first }
                     if (sequence !in seen) {
                         sequences[sequence] = sequences.getValue(sequence) + prices.last()
                         seen.add(sequence)
                     }
                 }
         }
         sequences
     }.values.max()

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
