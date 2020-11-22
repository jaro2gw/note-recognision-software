package model.element.impl

import model.element.api.AbstractElement
import org.opencv.core.Rect
import opencv.styles.Color

class Clef(rect: Rect) : AbstractElement(rect) {
    enum class Type(val note: Note.Name) {
        TREBLE(Note.Name.F),
        BASS(Note.Name.A)
    }

    var type: Type? = null

    override fun getLabel(): String = "Clef(${type ?: "?"})"

    override fun getColor(): Color = Color.RED
}