package opencv

import model.element.impl.Element
import model.element.impl.Head
import model.element.impl.Stave
import opencv.image.preprocessor.impl.*
import org.opencv.imgcodecs.Imgcodecs
import utils.showInWindow

object ImageProcessor {
    operator fun invoke(sourceFileName: String, targetFileName: String) {
        val source = Imgcodecs.imread(sourceFileName)!!
        assert(!source.empty()) { "Could not read image \"$sourceFileName\"" }
        source.showInWindow("Source")

        val gray = ToGrayscale(source)
        val negated = ToNegated(gray)
        val binary = ToBinary(negated)
        val rotate = ToRotated.byRadiansComputed(binary)
        val rotated = rotate(binary)
        val horizontal = ToHorizontalElements.byWidth(30.0).invoke(rotated)
        val vertical = ToVerticalElements(rotated)

        val staves = Stave.Detector(horizontal)
        val elements = Element.Detector(vertical)

        val circles = ToCircles(vertical)
        val heads = Head.Detector(circles)

        val debug = rotate(source)
        staves.forEach { it.drawOn(debug) }
        elements.forEach { it.drawOn(debug) }
        debug.showInWindow("Debug")

        staves.map { stave ->
            val elementsInStave = elements.filter { it in stave }
            val headsInStave = heads.filter { it in stave }
            return@map Triple(stave, elementsInStave, headsInStave)
        }
            .forEach { (stave, elements, heads) -> stave.assign(elements, heads) }

//        val notes = heads.mapNotNull { head ->
//            val element = elements.find { head.center in it.rect } ?: return@mapNotNull null
//            val note = Note(element.rect, head.center)
//            val array = vertical[head.center.x.toInt(), head.center.y.toInt()] ?: return@mapNotNull note
//
//            val full = array[0] == 255.0 // the matrix is negated
//            val tail = element.rect.width / head.rect.width > 1.25
//            val bar = element.rect.height / head.rect.height > 1.5
//
//            note.duration = when {
//                !bar -> Note.Duration.WHOLE
//                !full -> Note.Duration.HALF
//                !tail -> Note.Duration.QUARTER
//                else -> Note.Duration.EIGHT
//            }
//            return@mapNotNull note
//        }
//
//        val clefs = elements.minus(notes).map { Clef(it.rect) }
//
//        clefs.forEach { clef ->
//            val stave = staves.find { clef in it } ?: return@forEach
//            stave.assign(clef)
//        }
//
//        notes.forEach { note ->
//            val stave = staves.find { note in it } ?: return@forEach
//            stave.assign(note)
//        }

        val target = rotate(source)
        staves.forEach { it.drawOn(target) }
        target.showInWindow("Target")
        Imgcodecs.imwrite(targetFileName, target)
    }
}
