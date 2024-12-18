fun main() {
    fun List<String>.toComputer() = Computer(
        registerA = this[0].substringAfter(": ").toLong(),
        registerB = this[1].substringAfter(": ").toLong(),
        registerC = this[2].substringAfter(": ").toLong(),
        program = this[4].substringAfter(": ").split(",").map { it.toInt() }
    )

    val output = mutableListOf<Int>()

    fun Computer.run() {
        val code = program[pointer++]
        when (code) {
            0 -> {
                val numerator = registerA
                val denominator = 1.shl(getComboOperand().toInt())
                registerA = numerator / denominator
            }

            1 -> registerB = registerB xor getLiteral().toLong()
            2 -> registerB = getComboOperand() % 8
            3 -> if (registerA != 0L) pointer = getLiteral()
            4 -> {
                registerB = registerB xor registerC
                getLiteral()
            }

            5 -> output += (getComboOperand() % 8).toInt()
            6 -> {
                val numerator = registerA
                val denominator = 1.shl(getComboOperand().toInt())
                registerB = numerator / denominator
            }

            7 -> {
                val numerator = registerA
                val denominator = 1.shl(getComboOperand().toInt())
                registerC = numerator / denominator
            }
        }
    }

    fun Computer.runAll() {
        while (pointer + 1 < program.size) {
            this.run()
        }
    }

    fun part1(input: List<String>): List<Int> {
        return input.toComputer().runAll().let { output }
    }

    fun part2(input: List<String>): Long = input.toComputer().let { computer ->
        fun resetAndExec(a: Long) {
            computer.registerA = a
            computer.registerB = 0
            computer.registerC = 0
            computer.pointer = 0
            output.clear()
            computer.runAll()
        }

        var count = 3
        var targetOutput =  computer.program.takeLast(count)

        var a = 0L
        while (true) {
            resetAndExec(a)
            if (output == targetOutput) {
                if (count ==  computer.program.size) {
                    break
                }
                count++
                targetOutput =  computer.program.takeLast(count)
                a *= 8
            } else {
                a++
            }
        }
        a
    }

    val testInput = readInput("Day17_test")
    check(part1(testInput) == listOf(4, 6, 3, 5, 6, 3, 5, 2, 1, 0))

    val input = readInput("Day17")
    part1(input).println()
    part2(input).println()
}

data class Computer(
    var registerA: Long,
    var registerB: Long,
    var registerC: Long,
    val program: List<Int>,
    var pointer: Int = 0
) {

    fun Computer.getLiteral(): Int = program[pointer++]

    fun Computer.getComboOperand(): Long {
        return when (val value = program[pointer++]) {
            in 0..3 -> value.toLong()
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> error("Invalid value: $value")
        }
    }
}
