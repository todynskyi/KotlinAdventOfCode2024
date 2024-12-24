
fun main() {

    fun List<String>.toGates(): Pair<MutableList<Gate>, MutableMap<String, Int>>  {
        val registers = this.takeWhile { it.isNotBlank() }
            .associate { line -> line.split(": ").let { it.first() to it.last().toInt() } }

        val gates = this.dropWhile { it.isNotBlank() }
            .drop(1)
            .map { line -> line.split(" ").let { Gate(it[0], it[2], Operation.valueOf(it[1]), it[4]) } }
            .toMutableList()

        return gates to registers.toMutableMap()
    }

    fun Map<String, Int>.calculateWires(type: Char) =
        this.filter { it.key.startsWith(type) }
            .toList()
            .sortedBy { it.first }
            .map { it.second }
            .joinToString("")
            .reversed()
            .toLong(2)

    fun solve(gates: List<Gate>, registers: MutableMap<String, Int>): Long {
        val excluded = mutableSetOf<Gate>()
        while (excluded.size != gates.size) {
            val available = gates.filter { a ->
                a !in excluded && gates.none { b ->
                    (a.a == b.c || a.b == b.c) && b !in excluded
                }
            }
            for ((a, b, op, c) in available) {
                val v1 = registers.getOrDefault(a, 0)
                val v2 = registers.getOrDefault(b, 0)
                registers[c] = when (op) {
                    Operation.AND -> v1 and v2
                    Operation.OR -> v1 or v2
                    Operation.XOR -> v1 xor v2
                }
            }
            excluded.addAll(available)
        }

        return registers.calculateWires('z')
    }

    fun List<Gate>.firstZ(c: String): String? = filter { it.a == c || it.b == c }.let { x ->
        x.find { it.c.startsWith('z') }?.let { return "z" + (it.c.drop(1).toInt() - 1).toString().padStart(2, '0') }
        x.firstNotNullOfOrNull { firstZ(it.c) }
    }

    fun part1(input: List<String>): Long = input.toGates().let { (gates, registers) -> solve(gates, registers) }

    fun part2(input: List<String>): String {
        val (gates, registers) = input.toGates()

        val nxz = gates.filter { it.c.first() == 'z' && it.c != "z45" && it.op != Operation.XOR }
        val xnz = gates.filter { it.a.first() !in "xy" && it.b.first() !in "xy" && it.c.first() != 'z' && it.op == Operation.XOR }

        for (i in xnz) {
            val b = nxz.first { it.c == gates.firstZ(i.c) }
            val temp = i.c
            i.c = b.c
            b.c = temp
        }

        val carries = (registers.calculateWires('x') + registers.calculateWires('y') xor solve(gates, registers)).countTrailingZeroBits().toString()
        return (nxz + xnz + gates.filter { it.a.endsWith(carries) && it.b.endsWith(carries) })
            .map { it.c }
            .sorted()
            .joinToString(",")
    }

    val testInput = readInput("Day24_test")
    check(part1(testInput) == 2024L)

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}

enum class Operation { AND, OR, XOR }

data class Gate(val a: String, val b: String, val op: Operation, var c: String)
