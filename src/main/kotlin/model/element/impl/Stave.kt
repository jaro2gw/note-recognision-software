package model.element.impl

import model.detector.api.AbstractDetector
import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import styles.Color
import utils.intersects
import utils.middle
import utils.ys

typealias Line = Rect

class Stave(lines: Collection<Line>) : AbstractElement(contours = computeRectangle(lines)) {
    companion object Detector : AbstractDetector<Stave>() {
        private fun computeRectangle(rectangles: Iterable<Rect>): Rect {
            val xMin = rectangles.minOf { it.x }
            val xMax = rectangles.maxOf { it.x + it.width }
            val yMin = rectangles.minOf { it.y }
            val yMax = rectangles.maxOf { it.y + it.height }

            val width = xMax - xMin
            val height = yMax - yMin

            return Rect(xMin, yMin, width, height)
        }

        private fun computeBottomLine(lines: Collection<Line>): Double = lines.minOf { it.middle.y }

        private fun computeAverageSpace(lines: Collection<Line>): Double = lines.sortedBy { it.middle.y }
            .zipWithNext { lower, upper -> upper.middle.y - lower.middle.y }
            .average()

        override fun Mat.preprocessImage(): Mat {
            TODO("Not yet implemented")
        }

        override fun Mat.findElementContours(): Collection<Line> {
            TODO("Not yet implemented")
        }

        private infix fun Rect.mergeWith(other: Rect): Rect = computeRectangle(setOf(this, other))

        private fun Collection<Line>.stitchStaveLines(): Collection<Line> {
            val stitched = mutableSetOf<Line>()
            forEach { line ->
                val same = stitched.find { line.ys intersects it.ys }
                if (same == null) stitched += line
                else {
                    stitched -= same
                    stitched += same mergeWith line
                }
            }
            return stitched
        }

        override fun Collection<Line>.convertToElements(): Collection<Stave> = stitchStaveLines()
            .sortedBy { it.middle.y }
            .windowed(5)
            .map { Stave(it) }
    }

    init {
        require(lines.size == 5) { "Stave has to contain exactly 5 lines" }
    }

    val averageSpace = computeAverageSpace(lines)
    val bottomLine = computeBottomLine(lines)

    private val notes: MutableSet<Note> = mutableSetOf()

    private var clef: Clef? = null

    private fun positionOnStave(note: Note): Int {
        TODO("Not yet implemented")
    }

    fun assignNote(note: Note) {
        notes += note
        val bottom = clef?.type?.ordinal ?: return
        val ordinal = bottom + positionOnStave(note)
        val names = Note.Name.values()
        note.name = names[ordinal % names.size]
    }

    fun removeNote(note: Note) {
        notes -= note
        note.name = null
    }

    fun assignClef(clef: Clef) {
        this.clef = clef
        val lower = clef.contours.middle.apply { y = clef.contours.y.toDouble() }
        val upper = clef.contours.middle.apply { y = clef.contours.y.toDouble() + clef.contours.height }
        clef.type = if (lower in this.contours && upper in this.contours) {
            Clef.Type.BASS
        } else {
            Clef.Type.TREBLE
        }
        notes.forEach { assignNote(it) }
    }

    fun removeClef() {
        clef?.type = null
        clef = null
    }

    override fun getLabel(): String = "Stave"

    override fun getColor(): Scalar = Color.BLUE

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        clef?.drawOn(matrix)
        notes.forEach { it.drawOn(matrix) }
    }
}