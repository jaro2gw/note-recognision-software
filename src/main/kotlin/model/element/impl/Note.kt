package model.element.impl

import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect

class Note(rect: Rect) : AbstractElement(rect) {
    enum class Name {
        G, F, E, D, C, H, A
    }

    //    var head: Head? = null
    val center: Point = if (rect.width * 1.2 > rect.height) {
        Point(rect.x + rect.width / 2.0, rect.y + rect.height / 2.0)
    } else {
        Point(rect.x + rect.width / 2.0, rect.y + rect.height * 5.0 / 6.0)
    }

    var name: Name? = null
    var duration: Duration? = null

    enum class Duration(val value: Int) {
        WHOLE(1),
        HALF(2),
        QUARTER(4),
        EIGHT(8)
    }

    override fun getLabel(): String = "Note(${name ?: "?"},${duration?.value ?: "?"})"

    override fun getColor(): Color = Color.GREEN

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
//        head?.drawOn(matrix)
    }

    public fun assignDuration(black: (Point) -> Boolean) {
        duration = when {
            rect.width * 1.2 > rect.height -> {
                Duration.WHOLE
            }
            rect.width * 1.8 > rect.height -> {
                Duration.EIGHT
            }
            black(center) -> {
                Duration.QUARTER
            }
            else -> {
                Duration.HALF
            }
        }
    }
}