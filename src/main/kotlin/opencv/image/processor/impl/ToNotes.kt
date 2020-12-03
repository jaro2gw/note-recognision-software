package opencv.image.processor.impl

import model.element.impl.Element
import model.element.impl.Stave
import org.opencv.core.Mat
import org.opencv.core.Point
import java.util.*
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3

val ToNotes: (Mat) -> List<Pair<Operation, Mat>> = { matrix ->
    val results = LinkedList<Pair<Operation, Mat>>()
    results += Operation.SOURCE to matrix

    val gray = ToGrayscale(matrix).also { results += Operation.GRAYSCALE to it }
    val negated = ToNegated(gray).also { results += Operation.NEGATED to it }
    val binary = ToBinary(negated).also { results += Operation.BINARY to it }

    val rotate = ToRotated(binary)
    val rotated = rotate(binary).also { results += Operation.ROTATED to it }

//    val width = matrix.width() / 30.0
    val horizontal = ToHorizontalElements(150.0)(rotated).also { results += Operation.HORIZONTAL to it }
//    val height = matrix.height() / 90.0
    val vertical = ToVerticalElements(20.0)(rotated).also { results += Operation.VERTICAL to it }

    val staves = Stave.Detector(horizontal)
    val elements = Element.Detector(vertical)

    val debug = rotate(matrix).also { results += Operation.DEBUG to it }
    staves.forEach { it.drawOn(debug) }
    elements.forEach { it.drawOn(debug) }

    val target = rotate(matrix).also { results += Operation.TARGET to it }

    fun black(point: Point): Boolean {
        val x = point.x.toInt()
        val y = point.y.toInt()
        var sum = 0.0
        var count = 0
        for (i in -20..20) {
            val row = y + i
            val col = x + i
            if (row < 0 || row >= target.rows()) continue
            if (col < 0 || col >= target.cols()) continue
            val pixel = target[row, col] ?: continue
            ++count
            val (blue, green, red) = pixel
            sum += (blue + green + red) / 3
        }
        return sum / count < 75
    }

    staves.forEach { stave ->
        val matching = elements.filter { it in stave }
            .map { it.rect }
        stave.assign(matching) { black(it) }
    }

    staves.forEach { it.drawOn(target) }
    results
}