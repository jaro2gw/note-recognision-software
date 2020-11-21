package utils

import org.opencv.core.Point
import org.opencv.core.Rect

val Rect.center get() = Point(x + width / 2.0, y + height / 2.0)

operator fun Rect.compareTo(other: Rect): Int =
    if (x == other.x) y.compareTo(other.y)
    else x.compareTo(other.x)

fun Rect.prettyString() = "[x=$x,y=$y,w=$width,h=$height]"

val Rect.xs get() = x.rangeTo(x + width)
val Rect.ys get() = y.rangeTo(y + height)

infix fun Rect.intersects(other: Rect): Boolean = this.xs intersects other.xs && this.ys intersects other.ys
