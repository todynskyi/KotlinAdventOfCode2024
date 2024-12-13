fun main() {
    val buttonRegex = """Button .: X\+(\d+), Y\+(\d+)""".toRegex()
    val prizeRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

    fun String.toPoint(regex: Regex): Point {
        val (x, y) = regex.find(this)!!.destructured
        return Point(x.toInt(), y.toInt())
    }

    fun List<String>.toClawMachines(): List<ClawMachines> = this.chunked(4).map {
        ClawMachines(it[0].toPoint(buttonRegex), it[1].toPoint(buttonRegex), it[2].toPoint(prizeRegex))
    }

    fun part1(input: List<String>): Long = input.toClawMachines().sumOf { it.pushButtons() }

    fun part2(input: List<String>): Long = input.toClawMachines().sumOf { it.pushButtons(10000000000000) }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

data class ClawMachines(val buttonA: Point, val buttonB: Point, val prize: Point) {

    fun pushButtons(addPosition: Long = 0): Long {
        val prizeX = prize.x + addPosition
        val prizeY = prize.y + addPosition
        val b = (prizeY * buttonA.x - buttonA.y * prizeX) / (-buttonA.y * buttonB.x + buttonA.x * buttonB.y)
        val a = (prizeX - buttonB.x * b) / buttonA.x
        return if (a * buttonA.x + b * buttonB.x == prizeX && a * buttonA.y + b * buttonB.y == prizeY) {
            a * 3 + b
        } else {
            0
        }
    }
}
