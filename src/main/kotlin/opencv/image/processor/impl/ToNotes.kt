package opencv.image.processor.impl

import model.element.impl.Element
import model.element.impl.Head
import model.element.impl.Stave
import org.opencv.core.Mat
import java.util.*

object ToNotes : (Mat) -> List<Pair<Operation, Mat>> {
    override fun invoke(mat: Mat): List<Pair<Operation, Mat>> {
        val results = LinkedList<Pair<Operation, Mat>>()
        results += Operation.SOURCE to mat
        val gray = ToGrayscale(mat).also { results += Operation.GRAYSCALE to it }
        val negated = ToNegated(gray).also { results += Operation.NEGATED to it }
        val binary = ToBinary(negated).also { results += Operation.BINARY to it }
        val rotate = ToRotated.byRadiansComputed(binary)
        val rotated = rotate(binary).also { results += Operation.ROTATED to it }
        val horizontal = ToHorizontalElements.byWidth(100.0)
            .invoke(rotated)
            .also { results += Operation.HORIZONTAL to it }
        val vertical = ToVerticalElements(rotated).also { results += Operation.VERTICAL to it }

        val staves = Stave.Detector(horizontal)
        val elements = Element.Detector(vertical)

        val circles = ToCircles(vertical).also { results += Operation.CIRCLES to it }
        val heads = Head.Detector(circles)

        val debug = rotate(mat).also { results += Operation.DEBUG to it }
        staves.forEach { it.drawOn(debug) }
        elements.forEach { it.drawOn(debug) }

        staves
            .map { stave ->
                val elementsInStave = elements.filter { it in stave }
                val headsInStave = heads.filter { it in stave }
                return@map Triple(stave, elementsInStave, headsInStave)
            }
            .forEach { (stave, elements, heads) ->
                stave.assign(elements, heads)
            }

        val target = rotate(mat).also { results += Operation.TARGET to it }
        staves.forEach { it.drawOn(target) }
        return results
    }
}