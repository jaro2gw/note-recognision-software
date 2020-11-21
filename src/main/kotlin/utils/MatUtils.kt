package utils

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import kotlin.math.PI

fun Mat.toBGR(): Mat {
    val matrix = clone()!!
    if (matrix.channels() < 3) {
        Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_GRAY2BGR)
    }
    return matrix
}

fun Mat.toGray(): Mat {
    val matrix = clone()!!
    if (matrix.channels() == 3) {
        Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY)
    }
    return matrix
}

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

fun Mat.toBinary(
    adaptiveMethod: Int,
    thresholdType: Int,
    blockSize: Int,
    constant: Double
): Mat {
    val binary = Mat()
    Imgproc.adaptiveThreshold(
        this,
        binary,
        255.0,
        adaptiveMethod,
        thresholdType,
        blockSize,
        constant
    )
    return binary
}

fun Mat.computeRotationAngle(): Double {
    val edges = Mat()
    Imgproc.Canny(
        this,
        edges,
        50.0,
        200.0,
        3,
        false
    )
    val lines = Mat()
    Imgproc.HoughLines(
        edges, lines,
        1.0,
        PI / 180,
        150
    )
    return lines.colIterable(0)
        .map { it[1] }
        .median()
}

//img = cv.imread('messi5.jpg',0)
//rows,cols = img.shape
//# cols-1 and rows-1 are the coordinate limits.
//M = cv.getRotationMatrix2D(((cols-1)/2.0,(rows-1)/2.0),90,1)
//dst = cv.warpAffine(img,M,(cols,rows))

val Mat.center get() = Point(cols() / 2.0, rows() / 2.0)

fun Mat.rotate(angle: Double, scale: Double = 1.0): Mat {
    val rotated = Mat()
    val rotation = Imgproc.getRotationMatrix2D(center, angle, scale)
    Imgproc.warpAffine(this, rotated, rotation, size())
    return rotated
}