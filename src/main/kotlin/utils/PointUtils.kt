package utils

import org.opencv.core.Point
import org.opencv.core.Rect

fun Point.insideRect(rectangle: Rect): Boolean = rectangle.contains(this)

operator fun Point.component1() = x
operator fun Point.component2() = y