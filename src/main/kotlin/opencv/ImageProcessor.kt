package opencv

import model.element.impl.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import utils.*

object ImageProcessor {
    operator fun invoke(sourceFileName: String, targetFileName: String) {
        val source = Imgcodecs.imread(sourceFileName)!!
        assert(!source.empty()) { "Could not read image \"$sourceFileName\"" }
        source.showInWindow("Source")

        val gray = source.toGray()
        gray.showInWindow("Gray")

        val binary = gray.negate().toBinary(
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            15,
            -2.0
        )
        binary.showInWindow("Binary")

//        val angle = binary.computeRotationAngle()
//        val rotated = binary.rotate(-angle)
//        showImage("Rotated", rotated)
//        val target = source.rotate(-angle)

        val angle = binary.computeRotationAngle()
        val rotated = binary.rotate(angle)
        rotated.showInWindow("Rotated")
        val final = rotated.clone()!!
        val target = source.clone()!!

        val staves = StaveLines.Detector(final)

        val elements = StaveElement.Detector(final)
        val heads = NoteHead.Detector(final)

        val notes = heads.mapNotNull { head ->
            val element = elements.find { head.center in it.box } ?: return@mapNotNull null
            val note = Note(element.box, head.center)
            val full = final[head.center.x.toInt(), head.center.y.toInt()][0] > 200 // the matrix is negated
            val tail = element.box.width / head.box.width > 1.25
            val bar = element.box.height / head.box.height > 1.5
            note.duration = when {
                !bar -> Note.Duration.WHOLE
                !full -> Note.Duration.HALF
                !tail -> Note.Duration.QUARTER
                else -> Note.Duration.EIGHT
            }
            return@mapNotNull note
        }

        val clefs = elements.minus(notes).map { Clef(it.box) }

        clefs.forEach { clef ->
            val stave = staves.find { clef in it } ?: return@forEach
            stave.assign(clef)
        }

        notes.forEach { note ->
            val stave = staves.find { note in it } ?: return@forEach
            stave.assign(note)
        }

        staves.forEach { it.drawOn(target) }
        target.showInWindow("Target")
        Imgcodecs.imwrite(targetFileName, target)
    }
}

