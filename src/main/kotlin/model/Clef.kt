package model

import org.opencv.core.Rect
import org.opencv.core.Scalar
import styles.Color

class Clef(rectangle: Rect) : Element(rectangle) {
    enum class Type(bottomLineNote: Note.Name) {
        TREBLE(Note.Name.E),
        BASS(Note.Name.G)
    }

    lateinit var type: Type

    override fun getLabel(): String = "Clef($type)"

    override fun getColor(): Scalar = Color.RED
}