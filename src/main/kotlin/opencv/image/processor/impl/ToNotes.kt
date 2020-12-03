package opencv.image.processor.impl

import model.element.impl.Element
import model.element.impl.Head
import model.element.impl.Stave
import org.opencv.core.Mat
import java.util.*

val ToNotes: (Mat) -> List<Pair<Operation, Mat>> = { matrix ->
    val results = LinkedList<Pair<Operation, Mat>>()
    results += Operation.SOURCE to matrix

    val gray = ToGrayscale(matrix).also { results += Operation.GRAYSCALE to it }
    val negated = ToNegated(gray).also { results += Operation.NEGATED to it }
    val binary = ToBinary(negated).also { results += Operation.BINARY to it }

    val rotate = ToRotated(binary)
    val rotated = rotate(binary).also { results += Operation.ROTATED to it }

//    val width = matrix.width() / 30.0
    val horizontal = ToHorizontalElements(100.0)(rotated).also { results += Operation.HORIZONTAL to it }
//    val height = matrix.height() / 90.0
    val vertical = ToVerticalElements(20.0)(rotated).also { results += Operation.VERTICAL to it }

    val staves = Stave.Detector(horizontal)
    val elements = Element.Detector(vertical)

//    val radius = 28.0
    val circles = ToHorizontalElements(15.0)(vertical).also { results += Operation.CIRCLES to it }
    val heads = Head.Detector(circles)

    val debug = rotate(matrix).also { results += Operation.DEBUG to it }
    staves.forEach { it.drawOn(debug) }
    elements.forEach { it.drawOn(debug) }
    heads.forEach { it.drawOn(debug) }

    staves
        .map { stave ->
            val elementsInStave = elements.filter { it in stave }
            val headsInStave = heads.filter { it in stave }
            return@map Triple(stave, elementsInStave, headsInStave)
        }
        .forEach { (stave, elements, heads) ->
            stave.assign(elements, heads)
        }

    val target = rotate(matrix).also { results += Operation.TARGET to it }
    staves.forEach { it.drawOn(target) }
    results
}