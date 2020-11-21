package model.element.impl

import model.element.api.AbstractElement
import model.element.api.ContourBasedDetector
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import styles.Color

open class StaveElement(contours: Rect) : AbstractElement(contours) {
    companion object Detector : ContourBasedDetector<StaveElement>() {
        override fun Mat.preprocessImage(): Mat {
            val copy = clone()
            val size = copy.rows() / 30
            val structure = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                Size(1.0, size.toDouble())
            )
            Imgproc.erode(copy, copy, structure)
            Imgproc.dilate(copy, copy, structure)
            return copy
        }

        override fun Mat.findContours(): Collection<Rect> {
            TODO("Not yet implemented")
        }

        override fun Collection<Rect>.convertToElements(): Collection<StaveElement> = map {
            StaveElement(it)
        }
    }

    override fun getLabel(): String = "StaveElement"

    override fun getColor(): Color = Color.ALMOND
}