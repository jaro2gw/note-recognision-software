package model

import org.opencv.core.Mat
import org.opencv.core.Rect

abstract class Element(
    val rectangle: Rect,
    val matrix: Mat
)