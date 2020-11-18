package model

import org.opencv.core.Rect
import org.opencv.core.Scalar
import styles.Color
import utils.intersects
import utils.ys

class Stave private constructor(
    rectangle: Rect,
    val bottomLine: Double,
    val averageSpace: Double
) : Element(rectangle) {
    companion object {
        private fun sameLine(line1: Rect, line2: Rect): Boolean = line1.ys intersects line2.ys

        private fun stitchTogether(lines: Collection<Rect>): Collection<Rect> {
            TODO("Needs implementation")
        }

        private fun computeRectangle(rectangles: Iterable<Rect>): Rect {
            val xMin = rectangles.minOf { it.x }
            val xMax = rectangles.maxOf { it.x + it.width }
            val yMin = rectangles.minOf { it.y }
            val yMax = rectangles.maxOf { it.y + it.height }

            val width = xMax - xMin
            val height = yMax - yMin

            return Rect(xMin, yMin, width, height)
        }

        private fun computeBottomLine(lines: Collection<Rect>): Double {
            TODO("Needs implementation")
        }

        private fun computeAverageSpace(lines: Collection<Rect>): Double {
            TODO("Needs implementation")
        }

        fun of(lines: Collection<Rect>, needsStitching: Boolean = true): Stave {
            if (needsStitching) {
                val stitched = stitchTogether(lines)
                return of(stitched, false)
            }
            val rectangle = computeRectangle(lines)
            val bottomLine = computeBottomLine(lines)
            val averageSpace = computeAverageSpace(lines)
            return Stave(rectangle, bottomLine, averageSpace)
        }
    }

    lateinit var type: Clef.Type

    override fun getLabel(): String = "Stave($type)"

    override fun getColor(): Scalar = Color.BLUE
}