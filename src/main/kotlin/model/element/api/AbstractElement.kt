package model.element.api

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.middle
import utils.prettyString

abstract class AbstractElement(val contours: Rect) {
    abstract fun getLabel(): String

    abstract fun getColor(): Color

    private fun drawContoursOn(matrix: Mat) {
        val color = getColor()
        Imgproc.rectangle(
            matrix,
            contours,
            color,
            2,
            Imgproc.LINE_8
        )
    }

    private fun drawLabelOn(matrix: Mat) {
        val label = getLabel()
        val anchor = Point(contours.x.toDouble(), contours.y.toDouble() + contours.height + 5)
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

    open fun drawOn(matrix: Mat) {
        drawContoursOn(matrix)
        drawLabelOn(matrix)
    }

    operator fun contains(other: AbstractElement): Boolean {
        return other.contours.middle in contours
    }

    override fun toString(): String = getLabel() + '@' + contours.prettyString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractElement

        if (contours != other.contours) return false

        return true
    }

    override fun hashCode(): Int = contours.hashCode()
}