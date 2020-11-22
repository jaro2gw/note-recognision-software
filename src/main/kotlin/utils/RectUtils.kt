package utils

import org.opencv.core.Point
import org.opencv.core.Rect
import kotlin.math.max
import kotlin.math.min

val Rect.center get() = Point(x + width / 2.0, y + height / 2.0)

operator fun Rect.compareTo(other: Rect): Int =
    if (x == other.x) y.compareTo(other.y)
    else x.compareTo(other.x)

fun Rect.prettyString() = "[x=$x,y=$y,w=$width,h=$height]"

val Rect.xs get() = x.rangeTo(x + width)
val Rect.ys get() = y.rangeTo(y + height)

infix fun Rect.intersects(other: Rect): Boolean = this.xs intersects other.xs && this.ys intersects other.ys

infix fun Rect.mergeWith(other: Rect): Rect {
    val xMin = min(x, other.x)
    val xMax = max(x + width, other.x + other.width)
    val yMin = min(y, other.y)
    val yMax = max(y + height, other.y + other.height)
    return Rect(xMin, yMin, xMax - xMin, yMax - yMin)
}
