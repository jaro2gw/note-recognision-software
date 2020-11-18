package model

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import prettyString

class Note(
    rectangle: Rect,
    matrix: Mat,
    val center: Point,
    var name: Name = Name.UNKNOWN,
    var duration: Duration = Duration.UNKNOWN
) : Element(rectangle, matrix) {
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

    override fun toString(): String {
        return "Note($name,$duration@${rectangle.prettyString()})"
    }
}