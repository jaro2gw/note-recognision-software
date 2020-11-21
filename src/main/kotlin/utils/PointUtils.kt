package utils

import org.opencv.core.Point

operator fun Point.component1() = x
operator fun Point.component2() = y