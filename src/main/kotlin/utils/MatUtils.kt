package utils

import org.opencv.core.*
import org.opencv.highgui.HighGui
import org.opencv.imgproc.Imgproc
import kotlin.math.PI

fun Mat.colIterator(col: Int): Iterator<DoubleArray> = iterator {
    for (row in 0 until rows()) yield(this@colIterator[row, col])
}

fun Mat.colIterable(col: Int): Iterable<DoubleArray> = Iterable { colIterator(col) }

fun Mat.rowIterator(row: Int): Iterator<DoubleArray> = iterator {
    for (col in 0 until cols()) yield(this@rowIterator[row, col])
}

fun Mat.rowIterable(row: Int): Iterable<DoubleArray> = Iterable { rowIterator(row) }

fun Mat.crop(rectangle: Rect): Mat = submat(
        rectangle.y,
        rectangle.y + rectangle.height,
        rectangle.x,
        rectangle.x + rectangle.width
)

fun Mat.negate(): Mat {
    val not = clone()
    Core.bitwise_not(not, not)
    return not
}

val Mat.center get() = Point(cols() / 2.0, rows() / 2.0)

fun Mat.showInWindow(name: String) {
    HighGui.imshow(name, this)
    HighGui.waitKey()
    HighGui.destroyWindow(name)
}