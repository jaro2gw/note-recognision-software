package model.element.impl

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import styles.Color

class Note(contours: Rect, val center: Point) : StaveElement(contours) {
    enum class Name {
        A, B, C, D, E, F, G
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
        val color = getColor()
        Imgproc.circle(matrix, center, 1, color, 3, Imgproc.LINE_AA)
    }
}