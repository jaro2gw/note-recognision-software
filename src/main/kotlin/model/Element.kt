package model

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.middle
import utils.prettyString

abstract class Element(val rectangle: Rect) {
    abstract fun getLabel(): String

    abstract fun getColor(): Scalar

    fun drawContours(matrix: Mat) {
        val color = getColor()
        Imgproc.rectangle(
            matrix,
            rectangle,
            color,
            2,
            Imgproc.LINE_8
        )
    }

    fun drawLabel(matrix: Mat) {
        val label = getLabel()
        val anchor = Point(rectangle.x.toDouble(), rectangle.y.toDouble() + rectangle.height)
        Imgproc.putText(
            matrix,
            label,
            anchor,
            Imgproc.FONT_HERSHEY_COMPLEX,
            8.0,
            Color.BLACK,
            2,
            Imgproc.LINE_8
        )
    }

    fun drawAll(matrix: Mat) {
        drawContours(matrix)
        drawLabel(matrix)
    }

    operator fun contains(other: Element): Boolean {
        return other.rectangle.middle in rectangle
    }

    override fun toString(): String = getLabel() + '@' + rectangle.prettyString()
}