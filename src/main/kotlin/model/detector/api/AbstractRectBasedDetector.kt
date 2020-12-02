package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.intersects
import utils.mergeWith

abstract class AbstractRectBasedDetector<T : AbstractElement> : AbstractDetector<T>() {
    abstract fun convertToElements(boxes: Collection<Rect>): Collection<T>

    override fun invoke(matrix: Mat): Collection<T> {
        val points = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            matrix,
            points,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        val contours = points.map { Imgproc.boundingRect(it) }
        val groups = mutableSetOf<Rect>()
        contours.forEach { contour ->
            val same = groups.find { it intersects contour }
            if (same == null) groups += contour
            else {
                groups -= same
                groups += same mergeWith contour
            }
        }
        return convertToElements(groups)
    }
}