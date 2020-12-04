package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.expand
import utils.intersects
import utils.mergeWith


abstract class AbstractRectBasedDetector<T : AbstractElement> : AbstractDetector<T>() {
    abstract fun convertToElements(boxes: Collection<Rect>): Collection<T>

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
            .filter { it.width >= 30 || it.height >= 30 }
    }

    override fun invoke(matrix: Mat): Collection<T> {
        val contours = contours(matrix)
        return convertToElements(contours)
    }
}