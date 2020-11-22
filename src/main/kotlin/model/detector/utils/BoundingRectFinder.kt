package model.detector.utils

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc

object BoundingRectFinder : (Mat) -> Collection<Rect> {
    override fun invoke(matrix: Mat): Collection<Rect> {
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            matrix,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours.map { Imgproc.boundingRect(it) }
    }
}