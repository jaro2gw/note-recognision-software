package model

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect

data class Note(
    override val rectangle: Rect,
    override val matrix: Mat,
    val center: Point,
    var duration: Duration = Duration.UNKNOWN,
    var name: Name = Name.UNKNOWN
) : Element(rectangle, matrix, Type.NOTE) {
    enum class Name {
        A, B, C, D, E, F, G, UNKNOWN
    }

    enum class Duration {
        WHOLE,
        HALF,
        QUARTER,
        EIGHTH,
        UNKNOWN
    }
}