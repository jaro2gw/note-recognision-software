package model.element.impl

import model.detector.api.AbstractDetector
import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.core.Scalar
import styles.Color

class Clef(rectangle: Rect) : AbstractElement(rectangle) {
    companion object Detector : AbstractDetector<Clef>() {
        override fun Mat.preprocessImage(): Mat {
            TODO("Not yet implemented")
        }

        override fun Mat.findElementContours(): Collection<Rect> {
            TODO("Not yet implemented")
        }

        override fun Collection<Rect>.convertToElements(): Collection<Clef> = map { Clef(it) }
    }

    enum class Type(bottomLineNote: Note.Name) {
        TREBLE(Note.Name.E),
        BASS(Note.Name.G)
    }

    var type: Type? = null

    override fun getLabel(): String = "Clef(${type ?: "TBD"})"

    override fun getColor(): Scalar = Color.RED
}