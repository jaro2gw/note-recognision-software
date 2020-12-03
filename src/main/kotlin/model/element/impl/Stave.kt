package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import utils.*

class Stave(lineBoxes: Collection<Rect>) :
    AbstractElement(rect = computeRectangle(lineBoxes)) {
    companion object Detector : AbstractRectBasedDetector<Stave>() {
        private fun computeRectangle(lines: Iterable<Rect>): Rect = lines.reduce(Rect::mergeWith)

        private fun stitchStaveLines(lines: Collection<Rect>): Collection<Rect> {
            val stitched = mutableSetOf<Rect>()
            lines.forEach { line ->
                val same = stitched.filter { line.ys intersects it.ys }
                if (same.isEmpty()) stitched += line
                else {
                    stitched -= same
                    stitched += same.fold(line, Rect::mergeWith)
                }
            }
            return stitched
        }

        override fun convertToElements(boxes: Collection<Rect>): Collection<Stave> = stitchStaveLines(boxes)
            .sortedBy { it.center.y }
            .filter { it.width >= 300 }
            .chunked(5) { Stave(it) }
    }

    private val lines = lineBoxes.sortedBy { it.center.y }
    private val notes: MutableSet<Note> = mutableSetOf()
    private var clef: Clef? = null

    private fun findClosestLines(center: Point): IndexedValue<Pair<Rect, Rect>>? = lines.zipWithNext()
        .withIndex()
        .find { (_, lines) ->
            val (lower, upper) = lines
            val above = center.y >= lower.center.y
            val below = center.y <= upper.center.y
            return@find above && below
        }

    private fun positionOnStave(note: Note): Int? {
        val center = note.center
        val (index, lines) = findClosestLines(center) ?: return null
        val (lower, upper) = lines
        val space = upper.center.y - lower.center.y
        val position = (center.y - lower.center.y) / space
        return 2 * index + when {
            position <= 0.3 -> 0
            position <= 0.7 -> 1
            else -> 2
        }
    }

    fun assign(elements: Collection<Element>, black: (Point) -> Boolean) {
        val sorted = elements.sortedBy { it.rect.center.x }
        val clef = sorted.firstOrNull()?.let { Clef(it.rect) } ?: return
        assign(clef)
        sorted.drop(1)
            .map { Note(it.rect) }
            .onEach { it.assignDuration(black) }
            .forEach { assign(it) }
    }

    private fun assign(note: Note) {
        notes += note
        val position = positionOnStave(note) ?: return
        val bottom = clef?.type?.note?.ordinal ?: return
        val ordinal = bottom + position
        val names = Note.Name.values()
        note.name = names[ordinal % names.size]
    }

    private fun assign(clef: Clef) {
        val lower = clef.rect.center.apply { y = clef.rect.y.toDouble() }
        val upper = clef.rect.center.apply { y = clef.rect.y.toDouble() + clef.rect.height }
        clef.type = if (lower !in rect && upper !in rect) {
            Clef.Type.TREBLE
        } else {
            Clef.Type.BASS
        }
        this.clef = clef
    }

    override fun getLabel(): String = "Stave"

    override fun getColor(): Color = Color.BLUE

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        notes.forEach { it.drawOn(matrix) }
        clef?.drawOn(matrix)
    }
}