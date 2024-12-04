fun main() {

    fun String.mulInstructions(): Long {
        return this
            .split("mul(")
            .asSequence()
            .filter { it.indexOf(")") != -1 }
            .map { it.substringBefore(")") }
            .filter { it.contains(",") }
            .map { it.split(",").filter { num -> num.isNotBlank() } }
            .filter { it.size == 2 && it[0].isNumeric() && it[1].isNumeric() }
            .sumOf { it[0].toLong() * it[1].toLong() }
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { it.mulInstructions() }
    }

    fun part2(input: List<String>): Int {
        val regex = Regex("""(mul\(\d{1,3},\d{1,3}\))|(do\(\))|(don't\(\))""")
        var enabled = true
        return regex.findAll(input.joinToString(""))
            .map {
                when (it.value) {
                    "do()" -> {
                        enabled = true
                        0
                    }

                    "don't()" -> {
                        enabled = false
                        0
                    }

                    else -> {
                        if (enabled) {
                            val (num1, num2) = it.value.substringAfter("(")
                                .substringBefore(")")
                                .split(",")
                                .map { num -> num.toInt() }
                            num1 * num2
                        } else {
                            0
                        }
                    }
                }
            }.sum()
    }

    check(part1(readInput("Day03_test_part1")) == 161L)
    check(part2(readInput("Day03_test_part2")) == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
