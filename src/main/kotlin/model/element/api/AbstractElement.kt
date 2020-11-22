package model.element.api

import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.intersects
import utils.prettyString

abstract class AbstractElement(val rect: Rect) {
    abstract fun getLabel(): String

    abstract fun getColor(): Color

    protected fun drawContoursOn(matrix: Mat) {
        val color = getColor()
        Imgproc.rectangle(
            matrix,
            rect,
            color,
            2,
            Imgproc.LINE_8
        )
    }

    private fun drawLabelOn(matrix: Mat) {
        val label = getLabel()
        val anchor = Point(rect.x.toDouble(), rect.y.toDouble() - 5)
        Imgproc.putText(
            matrix,
            label,
            anchor,
            Imgproc.FONT_HERSHEY_SIMPLEX,
            0.5,
            Color.ORANGE,
            2,
            Imgproc.LINE_AA
        )
    }

    open fun drawOn(matrix: Mat) {
        drawContoursOn(matrix)
        drawLabelOn(matrix)
    }

    override fun toString(): String = getLabel() + '@' + rect.prettyString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractElement

        if (rect != other.rect) return false

        return true
    }

    override fun hashCode(): Int = rect.hashCode()

    operator fun component1() = rect

    operator fun contains(other: AbstractElement) = other.rect intersects this.rect
}