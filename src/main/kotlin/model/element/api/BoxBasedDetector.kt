package model.element.api

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc

abstract class BoxBasedDetector<T : AbstractElement> : AbstractDetector<T>() {
    private fun Mat.findContours(): Collection<MatOfPoint> {
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(
            this,
            contours,
            hierarchy,
            Imgproc.RETR_TREE,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        return contours
    }

    private fun Collection<MatOfPoint>.convertToBoundingBoxes(): Collection<Box> = map {
        val curve = MatOfPoint2f(*it.toArray())
        val poly = MatOfPoint2f()
        Imgproc.approxPolyDP(curve, poly, 3.0, true)
        val points = MatOfPoint(*poly.toArray())
        return@map Imgproc.boundingRect(points)
    }

    abstract fun Collection<Rect>.convertToElements(): Collection<T>

    override fun Mat.findElements(): Collection<T> = findContours().convertToBoundingBoxes().convertToElements()
}