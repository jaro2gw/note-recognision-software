package model

import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import styles.Color

class Note(
    rectangle: Rect,
    val center: Point
) : Element(rectangle) {
    enum class Name {
        A, B, C, D, E, F, G
    }

    lateinit var name: Name
    lateinit var duration: Duration

    enum class Duration {
        WHOLE,
        HALF,
        QUARTER,
        EIGHT
    }

    override fun getLabel(): String = "Note($name,$duration)"

    override fun getColor(): Scalar = Color.GREEN
}