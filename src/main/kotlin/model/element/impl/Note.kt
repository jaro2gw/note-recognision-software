package model.element.impl

import model.detector.api.AbstractDetector
import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import styles.Color

class Note(
    rectangle: Rect,
    val center: Point
) : AbstractElement(rectangle) {
    companion object Detector : AbstractDetector<Note>() {
        override fun Mat.preprocessImage(): Mat {
            TODO("Not yet implemented")
        }

        override fun Mat.findElementContours(): Collection<Rect> {
            TODO("Not yet implemented")
        }

        override fun Collection<Rect>.convertToElements(): Collection<Note> {
            TODO("Not yet implemented")
        }
    }

    enum class Name {
        A, B, C, D, E, F, G
    }

    var name: Name? = null
    var duration: Duration? = null

    enum class Duration {
        WHOLE,
        HALF,
        QUARTER,
        EIGHT
    }

    override fun getLabel(): String = "Note(${name ?: "TBD"},${duration ?: "TBD"})"

    override fun getColor(): Scalar = Color.GREEN

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        val color = getColor()
        Imgproc.circle(matrix, center, 2, color, 3, Imgproc.LINE_AA)
    }
}