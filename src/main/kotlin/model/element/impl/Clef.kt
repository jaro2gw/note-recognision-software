package model.element.impl

import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Rect

class Clef(rect: Rect, val type: Type) : AbstractElement(rect) {
    enum class Type(val note: Note.Name) {
        TREBLE(Note.Name.F),
        BASS(Note.Name.A)
    }

    override fun getLabel(): String = "Clef(${type})"

    override fun getColor(): Color = Color.RED
}