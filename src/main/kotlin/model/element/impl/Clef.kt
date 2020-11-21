package model.element.impl

import org.opencv.core.Rect
import styles.Color

class Clef(contours: Rect) : StaveElement(contours) {
    enum class Type(val note: Note.Name) {
        TREBLE(Note.Name.E),
        BASS(Note.Name.G)
    }

    var type: Type? = null

    override fun getLabel(): String = "Clef(${type ?: "?"})"

    override fun getColor(): Color = Color.RED
}