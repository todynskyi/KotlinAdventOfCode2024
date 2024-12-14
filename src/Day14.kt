fun main() {
    val regex = """^p=(\d+),(\d+) v=(-?\d+),(-?\d+)$""".toRegex()
    val predictMotion = Point(101, 103)

    fun List<String>.toRobots(): List<Robot> = this.map {
        val (px, py, vx, vy) = regex.find(it)!!.destructured.toList().map(String::toInt)
        Robot(position = Point(px, py), velocity = Point(vx, vy))
    }

    fun part1(input: List<String>): Int = input.toRobots()
        .map { it.move(100, predictMotion) }
        .groupingBy { it.quadrant(predictMotion.copy(x = predictMotion.x / 2, y = predictMotion.y / 2)) }
        .eachCount()
        .filterKeys { it != null }
        .values
        .reduce(Int::times)

    fun part2(input: List<String>): Int {
        var robots: List<Robot> = input.toRobots()
        var seconds = 0
        do {
            seconds++
            robots = robots.map { it.move(1, predictMotion) }
        } while (robots.distinctBy { it.position }.size != robots.size)

        return seconds
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}

data class Robot(val position: Point, val velocity: Point) {
    fun move(seconds: Int, area: Point): Robot {
        val v = (position + velocity.copy(x = velocity.x * seconds, y = velocity.y * seconds)).let {
            val nextX = it.x % area.x
            val nextY = it.y % area.y
            Point(x = if (nextX < 0) nextX + area.x else nextX, y = if (nextY < 0) nextY + area.y else nextY)
        }
        return copy(position = v)
    }

    fun quadrant(point: Point): Int? = when {
        position.x < point.x && position.y < point.y -> 1
        position.x > point.x && position.y < point.y -> 2
        position.x < point.x && position.y > point.y -> 3
        position.x > point.x && position.y > point.y -> 4
        else -> null
    }
}