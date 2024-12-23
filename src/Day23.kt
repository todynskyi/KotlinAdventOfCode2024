fun main() {

    fun List<String>.toConnections(): Map<String, Set<String>> = this.map { it.split("-") }
        .flatMap { (left, right) -> listOf(left to right, right to left) }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }

    fun Map<String, Set<String>>.getPassword(
        p: Set<String> = this.keys,
        r: Set<String> = emptySet(),
        x: Set<String> = emptySet()
    ): Set<String> =
        if (p.isEmpty() && x.isEmpty()) r
        else {
            val mostNeighbors: String = (p + x).maxBy { this.getValue(it).size }
            p.minus(this.getValue(mostNeighbors)).map { v ->
                getPassword(
                    p intersect this.getValue(v),
                    r + v,
                    x intersect this.getValue(v)
                )
            }.maxBy { it.size }
        }

    fun part1(input: List<String>): Int = input.toConnections().let { connections ->
        connections.keys
            .filter { it.startsWith("t") }
            .flatMap {
                connections.getValue(it)
                    .toList()
                    .let { list ->
                        list.flatMapIndexed { index, first ->
                            list.drop(index + 1).map { second ->
                                first to second
                            }
                        }
                    }
                    .filter { (a, b) -> b in connections.getValue(a) }
                    .map { (a, b) -> listOf(it, a, b).sorted() }
            }
            .distinct()
            .size
    }

    fun part2(input: List<String>): String = input.toConnections()
        .getPassword()
        .sorted()
        .joinToString(",")

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == "co,de,ka,ta")

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
