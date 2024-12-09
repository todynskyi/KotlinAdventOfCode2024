fun main() {
    fun toChecksum(id: Int, size: Int, blockId: Int): Long =
        (blockId..<(blockId + size)).sumOf { it.toLong() * id.toLong() }

    fun part1(input: String): Long {
        var sum: Long = 0

        var i = 0
        var blockId = 0
        var j = (input.length - 1) / 2 * 2
        var blockSize = 0
        var size = input[j].digitToInt()
        while (i <= j) {
            val fileSize = input[i].digitToInt()
            if (i % 2 == 0) {
                val fileId = i / 2
                sum += if (i == j) {
                    toChecksum(fileId, fileSize - blockSize, blockId)
                } else {
                    toChecksum(fileId, fileSize, blockId)
                }
            } else {
                var moved = 0
                while (moved < fileSize && j > i) {
                    sum += (blockId.toLong() + moved) * (j.toLong() / 2)
                    moved += 1
                    blockSize += 1
                    if (blockSize == size) {
                        j -= 2
                        blockSize = 0
                        size = input[j].digitToInt()
                    }
                }
            }

            blockId += fileSize
            i += 1
        }

        return sum
    }

    fun part2(input: String): Long {
        var sum: Long = 0
        var blockId = 0
        val fragments = input
            .map {
                val iSize = it.digitToInt()
                (blockId to iSize).also { blockId += iSize }
            }
            .toMutableList()

        var j = (input.length - 1) / 2 * 2
        while (j >= 0) {
            val fileSize = input[j].digitToInt()
            var moved = false
            val fileId = j / 2
            for (i in 1..<j step 2) {
                if (fragments[i].second >= fileSize) {
                    sum += toChecksum(fileId, fileSize, fragments[i].first)
                    fragments[i] = fragments[i].first + fileSize to fragments[i].second - fileSize
                    moved = true
                    break
                }
            }
            if (!moved) {
                sum += toChecksum(fileId, fileSize, fragments[j].first)
            }

            j -= 2
        }

        return sum
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput.first()) == 1928L)
    check(part2(testInput.first()) == 2858L)

    val input = readInput("Day09")
    part1(input.first()).println()
    part2(input.first()).println()
}