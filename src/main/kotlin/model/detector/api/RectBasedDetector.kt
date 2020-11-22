package model.detector.api

import model.detector.utils.BoundingRectFinder
import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.Rect
import utils.intersects
import utils.mergeWith

interface RectBasedDetector<T : AbstractElement> : ElementDetector<T> {
    fun convertToElements(boxes: Collection<Rect>): Collection<T>

    private fun groupBoxes(boxes: Collection<Rect>): Collection<Rect> {
        val elements = mutableSetOf<Rect>()
        boxes.forEach { box ->
            val same = elements.find { it intersects box }
            if (same == null) elements += box
            else {
                elements -= same
                elements += same mergeWith box
            }
        }
        return elements
    }

    override fun invoke(matrix: Mat): Collection<T> {
        val boxes = BoundingRectFinder(matrix)
        val grouped = groupBoxes(boxes)
        return convertToElements(grouped)
    }
}