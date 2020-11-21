package model.element.api

import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.prettyString

typealias Box = Rect

abstract class AbstractElement(val box: Box) {
    abstract fun getLabel(): String

    abstract fun getColor(): Color

    private fun drawContoursOn(matrix: Mat) {
        val color = getColor()
        Imgproc.rectangle(
            matrix,
            box,
            color,
            2,
            Imgproc.LINE_8
        )
    }

    private fun drawLabelOn(matrix: Mat) {
        val label = getLabel()
        val anchor = Point(box.x.toDouble(), box.y.toDouble() + box.height + 5)
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

    override fun toString(): String = getLabel() + '@' + box.prettyString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractElement

        if (box != other.box) return false

        return true
    }

    override fun hashCode(): Int = box.hashCode()
}