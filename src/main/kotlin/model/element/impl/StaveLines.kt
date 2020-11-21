package model.element.impl

import model.element.api.AbstractElement
import model.element.api.ContourBasedDetector
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.intersects
import utils.middle
import utils.ys

typealias Line = Rect

class StaveLines(lines: Collection<Line>) : AbstractElement(contours = computeRectangle(lines)) {
    companion object Detector : ContourBasedDetector<StaveLines>() {
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
            val processed = Mat(rows(), cols(), type())
            //TODO maybe find a better size value - a constant?
            val size = processed.cols() / 30
            val structure = Imgproc.getStructuringElement(
                Imgproc.MORPH_RECT,
                Size(size.toDouble(), 1.0)
            )
            Imgproc.erode(this, processed, structure)
            Imgproc.dilate(this, processed, structure)
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

        override fun Mat.findContours(): Collection<Rect> {
            TODO("Not yet implemented")
        }

        override fun Collection<Line>.convertToElements(): Collection<StaveLines> = stitchStaveLines()
            .sortedBy { it.middle.y }
            .windowed(5)
            .map { StaveLines(it) }
    }

    private val linesBottomToTop: List<Line>

    init {
        standardCheck(lines)
        linesBottomToTop = lines.sortedBy { it.middle.y }
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
            val aboveLower = note.center.y >= lower.middle.y
            val belowUpper = note.center.y <= upper.middle.y
            return@find aboveLower && belowUpper
        }

    operator fun contains(note: Note): Boolean = note.center in contours

    private fun positionOnStave(note: Note): Int? {
        val (index, lines) = findClosestLines(note) ?: return null
        val (lower, upper) = lines
        val space = upper.middle.y - lower.middle.y
        val position = (note.center.y - lower.middle.y) / space
        return 2 * index + when {
            position <= 0.33 -> 0
            position <= 0.66 -> 1
            else -> 2
        }
    }

    private fun assignElements(elements: Collection<StaveElement>, croppingFunction: (Rect) -> Mat) {
        if (elements.isEmpty()) return
        val startToEnd = elements.sortedBy { it.contours.middle.x }
        val clef = Clef(startToEnd[0].contours)
    }

    private fun assignNote(note: Note) {
        val position = positionOnStave(note) ?: return
        notes += note
        val bottom = clef?.type?.ordinal ?: return
        val ordinal = bottom + position
        val names = Note.Name.values()
        note.name = names[ordinal % names.size]
    }

//    fun removeNote(note: Note) {
//        notes -= note
//        note.name = null
//    }

    private fun assignClef(clef: Clef) {
        this.clef = clef
        val lower = clef.contours.middle.apply { y = clef.contours.y.toDouble() }
        val upper = clef.contours.middle.apply { y = clef.contours.y.toDouble() + clef.contours.height }
        clef.type = if (lower in this.contours && upper in this.contours) {
            Clef.Type.BASS
        } else {
            Clef.Type.TREBLE
        }
//        notes.forEach { assignNote(it) }
    }

    fun removeClef() {
        clef?.type = null
        clef = null
    }

    override fun getLabel(): String = "StaveLines"

    override fun getColor(): Color = Color.BLUE

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        clef?.drawOn(matrix)
        notes.forEach { it.drawOn(matrix) }
    }
}