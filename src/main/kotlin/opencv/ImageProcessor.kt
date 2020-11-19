package opencv

import model.element.impl.Clef
import model.element.impl.Note
import model.element.impl.Stave
import org.opencv.imgcodecs.Imgcodecs

object ImageProcessor {
    operator fun invoke(inputFileName: String, outputFileName: String) {
        val input = Imgcodecs.imread(inputFileName)!!
        val output = input.clone()!!

        //TODO convert to grayscale
        //TODO get rid of jitter
        //TODO rotate the output

        val staves = Stave.Detector(input)

        val clefs = Clef.Detector(input)
        clefs.forEach { clef ->
            staves.forEach { stave ->
                if (clef in stave) stave.assignClef(clef)
            }
        }

        val notes = Note.Detector(input)
        notes.forEach { note ->
            staves.forEach { stave ->
                if (note in stave) stave.assignNote(note)
            }
        }

        staves.forEach { it.drawOn(output) }
        Imgcodecs.imwrite(outputFileName, output)
    }
}