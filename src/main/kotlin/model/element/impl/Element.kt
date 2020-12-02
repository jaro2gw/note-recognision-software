package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Rect

class Element(rect: Rect) : AbstractElement(rect) {
    companion object Detector : AbstractRectBasedDetector<Element>() {
        override fun convertToElements(boxes: Collection<Rect>): Collection<Element> = boxes
            .filter { rect -> rect.width > 30 && rect.height > 30 }
            .map { Element(it) }
    }

    override fun getLabel(): String = "Element"

    override fun getColor(): Color = Color.ALMOND

    override fun drawOn(matrix: Mat) = drawContoursOn(matrix)
}