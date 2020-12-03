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

    private fun group(contours: Collection<Rect>): Collection<Rect> {
        val groups = mutableListOf<Rect>()
        contours.forEach { contour ->
            val same = groups.filter { it intersects contour }
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
        val offset = 15.0
        return contours(matrix)
            .map { (x, y, w, h) ->
                val lower = Point(x - offset, y - offset)
                val upper = Point(x + w + offset, y + h + offset)
                Rect(lower, upper)
            }
            .let { group(it) }
            .let { convertToElements(it) }
    }
}