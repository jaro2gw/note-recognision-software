package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Rect
import utils.expand
import utils.intersects
import utils.mergeWith

class Element(rect: Rect) : AbstractElement(rect) {
    companion object Detector : AbstractRectBasedDetector<Element>() {
        private fun groups(contours: Collection<Rect>): Collection<Rect> {
            val list = contours.map { it to it.expand(20.0) }
            val groups = mutableSetOf<Pair<Rect, Rect>>()
            list.forEach { (original, expanded) ->
                val same = groups.filter { it.second intersects expanded }
                groups -= same
                val (originals, oversized) = same.unzip()
                val newOriginal = originals.fold(original, Rect::mergeWith)
                val newOversized = oversized.fold(expanded, Rect::mergeWith)
                groups += newOriginal to newOversized
            }
            return groups.map { it.first }
        }

        override fun convertToElements(boxes: Collection<Rect>): Collection<Element> = groups(boxes)
            .filter { rect -> rect.width > 30 && rect.height > 30 }
            .map { Element(it) }
    }

    override fun getLabel(): String = "Element"

    override fun getColor(): Color = Color.ALMOND

    override fun drawOn(matrix: Mat) = drawContoursOn(matrix)
}