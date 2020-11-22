package model.element.impl

import model.detector.api.RectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Rect

class Element(rect: Rect) : AbstractElement(rect) {
    companion object Detector : RectBasedDetector<Element> {
        override fun convertToElements(boxes: Collection<Rect>): Collection<Element> = boxes.map { Element(it) }
    }

    override fun getLabel(): String = "Element"

    override fun getColor(): Color = Color.ALMOND

    override fun drawOn(matrix: Mat) = drawContoursOn(matrix)
}