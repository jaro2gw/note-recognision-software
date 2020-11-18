package utils

infix fun IntRange.intersects(other: IntRange): Boolean {
    return this.last >= other.first && this.first <= other.last
}