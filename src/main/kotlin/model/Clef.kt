package model

import org.opencv.core.Mat
import org.opencv.core.Rect
import prettyString

class Clef(
    rectangle: Rect,
    matrix: Mat,
    val type: Type
) : Element(rectangle, matrix) {
    enum class Type {
        TREBLE,
        BASS
    }

    override fun toString(): String = "Clef($type@${rectangle.prettyString()})"
}