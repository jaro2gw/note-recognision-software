package opencv

import model.element.impl.*
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import styles.Color
import utils.*

object ImageProcessor {
    private fun showImage(name: String, matrix: Mat) {
        HighGui.imshow(name, matrix)
        HighGui.waitKey()
        HighGui.destroyWindow(name)
    }

    operator fun invoke(sourceFileName: String, targetFileName: String) {
        val source = Imgcodecs.imread(sourceFileName)!!
        assert(!source.empty()) { "Could not read image \"$sourceFileName\"" }
        showImage("Source", source)

        val gray = source.toGray()
        showImage("Gray", gray)

        val binary = gray.negate().toBinary(
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            15,
            -2.0
        )
        showImage("Binary", binary)

        val angle = binary.computeRotationAngle()
        val rotated = binary.rotate(-angle)
        showImage("Rotated", rotated)

        val target = source.rotate(-angle)

        val staves = StaveLines.Detector(rotated)

        val elements = StaveElement.Detector(rotated)
        val heads = NoteHead.Detector(rotated)

        val notes = heads.mapNotNull { head ->
            val element = elements.find { head.center in it.contours } ?: return@mapNotNull null
            val note = Note(element.contours, head.center)
            val (b, g, r) = rotated[head.center.x.toInt(), head.center.y.toInt()]
            val full = Color(r, g, b) == Color.WHITE // the matrix is negated
            val tail = element.contours.width / head.contours.width > 1.25
            val bar = element.contours.height / head.contours.height > 1.5
            note.duration = when {
                !bar -> Note.Duration.WHOLE
                !full -> Note.Duration.HALF
                !tail -> Note.Duration.QUARTER
                else -> Note.Duration.EIGHT
            }
            return@mapNotNull note
        }

        val clefs = elements.minus(notes).map { Clef(it.contours) }

        clefs.forEach { clef ->

        }

        staves.forEach { it.drawOn(target) }
        showImage("Target", target)
        Imgcodecs.imwrite(targetFileName, target)
    }
}

