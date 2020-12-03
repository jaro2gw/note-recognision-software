package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import utils.*

class Stave(private val lines: Collection<Rect>) : AbstractElement(rect = computeRectangle(lines)) {
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
            .asSequence()
            .sortedBy { it.center.y }
            .filter { it.width >= 1000 }
            .chunked(5)
            .filter { it.size == 5 }
            .map { Stave(it) }
            .toList()
    }

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

    private fun findClefType(rect: Rect): Clef.Type? {
        if (!this.rect.intersects(rect)) return null
        val lower = rect.center.apply { y = rect.y.toDouble() }
        val upper = rect.center.apply { y = rect.y.toDouble() + rect.height }
        return if (lower !in this.rect && upper !in this.rect) Clef.Type.TREBLE
        else Clef.Type.BASS
    }

    private fun findClef(elements: Collection<Rect>): Clef? {
        if (elements.isEmpty()) return null
        val sorted = elements.sortedBy { it.center.x }
        val element = sorted[0]
        val type = findClefType(element) ?: return null
        return Clef(element, type)
    }

    fun assign(elements: Collection<Rect>, black: (Point) -> Boolean) {
        val clef = findClef(elements) ?: return
        this.clef = clef
        elements.filterNot { it intersects clef.rect }
            .map { Note(it) }
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

    override fun getLabel(): String = "Stave"

    override fun getColor(): Color = Color.BLUE

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        clef?.drawOn(matrix)
        notes.forEach { it.drawOn(matrix) }
    }
}