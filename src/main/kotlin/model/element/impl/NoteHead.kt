package model.element.impl

import model.element.api.AbstractDetector
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.component1
import utils.component2
import utils.rowIterable

data class NoteHead(val center: Point, val radius: Double) : StaveElement(contours = computeRectangle(center, radius)) {
    companion object Detector : AbstractDetector<NoteHead>() {
        private fun computeRectangle(center: Point, radius: Double): Rect {
            val (x, y) = center
            val lower = Point(x - radius, y - radius)
            val upper = Point(x + radius, y + radius)
            return Rect(lower, upper)
        }

        override fun Mat.preprocessImage(): Mat {
            val circles = Mat()
            Imgproc.HoughCircles(
                this,
                circles,
                Imgproc.HOUGH_GRADIENT,
                1.0,
                1.0,
                100.0,
                30.0,
                10
            )
            return circles
        }

        override fun Mat.findElements(): Collection<NoteHead> = rowIterable(0).map { (x, y, radius) ->
            val center = Point(x, y)
            NoteHead(center, radius)
        }
    }
}