package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import utils.center
import utils.component1
import utils.component2

class Head(val center: Point, private val radius: Double) : AbstractElement(rect = computeRectangle(center, radius)) {
    companion object Detector : AbstractRectBasedDetector<Head>() {
        private fun computeRectangle(center: Point, radius: Double): Rect {
            val (x, y) = center
            val lower = Point(x - radius, y - radius)
            val upper = Point(x + radius, y + radius)
            return Rect(lower, upper)
        }

        override fun convertToElements(boxes: Collection<Rect>): Collection<Head> =
            boxes.map { box -> Head(box.center, box.width / 2.0) }
    }

    override fun getLabel(): String = "Head"

    override fun getColor(): Color = Color.LAWN_GREEN

    override fun drawOn(matrix: Mat) {
        val color = getColor()
        Imgproc.circle(
            matrix,
            center,
            1,
            color,
            1,
            Imgproc.LINE_AA
        )
        Imgproc.circle(
            matrix,
            center,
            radius.toInt(),
            color,
            1,
            Imgproc.LINE_8
        )
    }

    operator fun component2() = center
}