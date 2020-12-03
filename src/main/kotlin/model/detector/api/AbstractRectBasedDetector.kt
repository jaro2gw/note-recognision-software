package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.*


abstract class AbstractRectBasedDetector<T : AbstractElement> : AbstractDetector<T>() {
    abstract fun convertToElements(boxes: Collection<Rect>): Collection<T>

    private fun offset(rect: Rect, v: Double): Rect {
        val (x, y, w, h) = rect
        val lower = Point(x - v, y - v)
        val upper = Point(x + w + v, y + h + v)
        return Rect(lower, upper)
    }

    private fun groups(contours: Collection<Rect>): Collection<Rect> {
        val groups = mutableListOf<Rect>()
        contours.forEach { contour ->
            val offset = offset(contour, 15.0)
            val same = groups.filter { it intersects offset }
            if (same.isEmpty()) groups += contour
            else {
                groups -= same
                groups += same.fold(contour, Rect::mergeWith)
            }
        }
        return groups
    }

    private fun contours(matrix: Mat): Collection<Rect> {
        val points = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            matrix,
            points,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return points.mapNotNull { Imgproc.boundingRect(it) }
    }

    override fun invoke(matrix: Mat): Collection<T> {
        val contours = contours(matrix)
        val groups = groups(contours)
        return convertToElements(groups)
    }
}