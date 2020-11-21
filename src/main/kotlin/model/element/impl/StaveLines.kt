package model.element.impl

import model.element.api.AbstractElement
import model.element.api.BoxBasedDetector
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.center
import utils.intersects
import utils.showInWindow
import utils.ys

typealias Line = Rect

class StaveLines(lines: Collection<Line>) : AbstractElement(box = computeRectangle(lines)) {
    companion object Detector : BoxBasedDetector<StaveLines>() {
        private fun standardCheck(lines: Collection<Line>) {
            require(lines.size == 5) { "Stave has to contain exactly 5 lines" }
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

        override fun Mat.preprocessImage(): Mat {
            val processed = clone()
            //TODO maybe find a better size value - a constant?
            val size = cols() / 30
            val structure = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                Size(size.toDouble(), 1.0)
            )
            Imgproc.erode(processed, processed, structure)
            Imgproc.dilate(processed, processed, structure)
            processed.showInWindow("Stave lines preprocessed")
            return processed
        }

        private infix fun Rect.mergeWith(other: Rect): Rect = computeRectangle(rectangles = listOf(this, other))

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

        override fun Collection<Line>.convertToElements(): Collection<StaveLines> = stitchStaveLines()
            .sortedBy { it.center.y }
            .windowed(5)
            .map { StaveLines(it) }
    }

    private val linesBottomToTop: List<Line>

    init {
        standardCheck(lines)
        linesBottomToTop = lines.sortedBy { it.center.y }
//        val spaces = linesBottomToTop.zipWithNext { lower, upper ->
//            upper.middle.y - lower.middle.y
//        }

        //TODO check spaces between lines and detect whether
        // the stave line is malformed or correct
        // using statistical methods (histogram, median)
    }

    private val notes: MutableSet<Note> = mutableSetOf()

    private var clef: Clef? = null

    private fun findClosestLines(note: Note): IndexedValue<Pair<Line, Line>>? = linesBottomToTop.zipWithNext()
        .withIndex()
        .find { (_, lines) ->
            val (lower, upper) = lines
            val aboveLower = note.center.y >= lower.center.y
            val belowUpper = note.center.y <= upper.center.y
            return@find aboveLower && belowUpper
        }

    private fun positionOnStave(note: Note): Int? {
        val (index, lines) = findClosestLines(note) ?: return null
        val (lower, upper) = lines
        val space = upper.center.y - lower.center.y
        val position = (note.center.y - lower.center.y) / space
        return 2 * index + when {
            position <= 0.33 -> 0
            position <= 0.66 -> 1
            else -> 2
        }
    }

    operator fun contains(note: Note): Boolean = note.center in box
    operator fun contains(clef: Clef): Boolean = clef.box.center in box

    fun assign(note: Note) {
        val position = positionOnStave(note) ?: return
        notes += note
        val bottom = clef?.type?.note?.ordinal ?: return
        val ordinal = bottom + position
        val names = Note.Name.values()
        note.name = names[ordinal % names.size]
    }

    fun assign(clef: Clef) {
        val lower = clef.box.center.apply { y = clef.box.y.toDouble() }
        val upper = clef.box.center.apply { y = clef.box.y.toDouble() + clef.box.height }
        clef.type = if (lower in this.box && upper in this.box) {
            Clef.Type.BASS
        } else {
            Clef.Type.TREBLE
        }
        this.clef = clef
    }

    override fun getLabel(): String = "StaveLines"

    override fun getColor(): Color = Color.BLUE

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        clef?.drawOn(matrix)
        notes.forEach { it.drawOn(matrix) }
    }
}