package utils

infix fun IntRange.intersects(other: IntRange): Boolean = this.last >= other.first && this.first <= other.last

fun Iterable<Double>.median(): Double {
    val sorted = sorted()
    if (sorted.isEmpty()) throw NoSuchElementException()
    val half = sorted.size / 2
    return if (sorted.size % 2 == 0) {
        val lower = sorted[half]
        val upper = sorted[half + 1]
        (lower + upper) / 2
    } else sorted[half]
}
