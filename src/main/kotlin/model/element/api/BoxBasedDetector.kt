package model.element.api

import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
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

    private fun Collection<MatOfPoint>.convertToBoundingBoxes(): Collection<Box> = map { Imgproc.boundingRect(it) }

    abstract fun Collection<Rect>.convertToElements(): Collection<T>

    override fun Mat.findElements(): Collection<T> = findContours().convertToBoundingBoxes().convertToElements()
}