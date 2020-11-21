package model.element.impl

import model.element.api.AbstractElement
import model.element.api.BoxBasedDetector
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.showInWindow

open class StaveElement(contours: Rect) : AbstractElement(contours) {
    companion object Detector : BoxBasedDetector<StaveElement>() {
        override fun Mat.preprocessImage(): Mat {
            val processed = clone()
            val structure = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                Size(1.0, 5.0)
            )
            Imgproc.erode(processed, processed, structure)
            Imgproc.dilate(processed, processed, structure)
            processed.showInWindow("Stave element preprocessed")
            return processed
        }

        override fun Collection<Rect>.convertToElements(): Collection<StaveElement> = map {
            StaveElement(it)
        }
    }

    override fun getLabel(): String = "StaveElement"

    override fun getColor(): Color = Color.ALMOND
}