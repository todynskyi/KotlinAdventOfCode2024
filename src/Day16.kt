import java.util.*


fun main() {
    fun List<String>.toGrid(): Map<Point, Char> =
        this.flatMapIndexed { y, row -> row.mapIndexed { x, c -> Point(x, y) to c } }
            .toMap()

    fun Map<Point, Char>.getKeyByValue(c: Char) = this.asIterable().first { it.value == c }.key

    fun Map<Point, Char>.solve1(): Int {
        val moves = mutableMapOf<State, Int>()
        val queue = PriorityQueue<Move> { e1, e2 -> e1.cost.compareTo(e2.cost) }
        queue.add(Move(State(this.getKeyByValue('S'), Point(1, 0)), 0))
        val endTile = this.getKeyByValue('E')
        while (true) {
            val (state, cost) = queue.poll()!!
            moves[state] = cost
            val (position, direction) = state
            if (position == endTile) return cost
            if (this[position + direction] != '#' && State(position + direction, direction) !in moves) queue.add(
                Move(
                    State(position + direction, direction),
                    cost + 1
                )
            )
            listOf(direction * Point(0, -1), direction * Point(0, 1))
                .filter { State(position, it) !in moves }
                .forEach { queue.add(Move(State(position, it), cost + 1000)) }
        }
    }

    fun Map<Point, Char>.solve2(): Int {
        val moves = mutableMapOf<State, Int>()
        val queue = PriorityQueue<Move> { e1, e2 -> e1.cost.compareTo(e2.cost) }
        val startTile = this.getKeyByValue('S')
        queue.add(Move(State(startTile, Point(1, 0)), 0, listOf(startTile)))
        val endTile = this.getKeyByValue('E')
        while (true) {
            val (state, cost, paths) = queue.poll()!!
            moves[state] = cost
            val (position, direction) = state
            if (position == endTile) {
                val tiles = paths.toMutableSet()
                generateSequence { queue.poll()!! }
                    .takeWhile { queue.isNotEmpty() && it.state.position == endTile && it.cost == cost }
                    .forEach { tiles.addAll(it.paths) }
                return tiles.size
            }
            if (this[position + direction] != '#' && State(position + direction, direction) !in moves) queue.add(
                Move(
                    State(position + direction, direction),
                    cost + 1,
                    paths + (position + direction)
                )
            )
            listOf(direction * Point(0, -1), direction * Point(0, 1))
                .filter { State(position, it) !in moves }
                .forEach { queue.add(Move(State(position, it), cost + 1000, paths)) }
        }
    }

    fun part1(input: List<String>): Int = input.toGrid()
        .solve1()

    fun part2(input: List<String>): Int = input.toGrid()
        .solve2()

    val testInput = readInput("Day16_test")
    check(part1(testInput) == 7036)
    check(part2(testInput) == 45)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}

data class State(val position: Point, val direction: Point)
data class Move(val state: State, val cost: Int, val paths: List<Point> = emptyList())
