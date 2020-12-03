package model.element.impl

import model.detector.api.AbstractRectBasedDetector
import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import utils.*

class Stave private constructor(private val lines: Collection<Rect>) : AbstractElement(rect = computeRectangle(lines)) {
    companion object Detector : AbstractRectBasedDetector<Stave>() {
        private fun computeRectangle(lines: Iterable<Rect>): Rect = lines.reduce(Rect::mergeWith)

        private fun stitchStaveLines(lines: Collection<Rect>): Collection<Rect> {
            val stitched = mutableSetOf<Rect>()
            lines.forEach { line ->
                val same = stitched.find { line.ys intersects it.ys }
                if (same == null) stitched += line
                else {
                    stitched -= same
                    stitched += same mergeWith line
                }
            }
            return stitched
        }

        override fun convertToElements(boxes: Collection<Rect>): Collection<Stave> = stitchStaveLines(boxes)
            .sortedBy { it.center.y }
                .filter { it.width >= 300 }
            .chunked(5) {
                Stave(it)
            }
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
        val rect = note.rect
        val x = rect.x + rect.width / 2.0
        val y = rect.y + rect.height * 5.0 / 6.0
        val center = Point(x, y)
        val (index, lines) = findClosestLines(center) ?: return null
        val (lower, upper) = lines
        val space = upper.center.y - lower.center.y
        val position = (center.y - lower.center.y) / space
        return 2 * index + when {
            position <= 0.33 -> 0
            position <= 0.66 -> 1
            else -> 2
        }
    }

    fun assign(elements: Collection<Element>, heads: Collection<Head>) {
        val sorted = elements.sortedBy { it.rect.x }
        val clef = sorted.firstOrNull()?.let { Clef(it.rect) } ?: return
        assign(clef)
        sorted.drop(1)
            .map { element ->
                val note = Note(element.rect)
                note.head = heads.find { it.center in element.rect }
                return@map note
            }
            .forEach { assign(it) }
    }

    private fun assign(note: Note) {
        val position = positionOnStave(note) ?: return
        notes += note
        val bottom = clef?.type?.note?.ordinal ?: return
        val ordinal = bottom + position
        val names = Note.Name.values()
        note.name = names[ordinal % names.size]
    }

    private fun assign(clef: Clef) {
        val lower = clef.rect.center.apply { y = clef.rect.y.toDouble() }
        val upper = clef.rect.center.apply { y = clef.rect.y.toDouble() + clef.rect.height }
        clef.type = if (lower in this.rect && upper in this.rect) {
            Clef.Type.BASS
        } else {
            Clef.Type.TREBLE
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