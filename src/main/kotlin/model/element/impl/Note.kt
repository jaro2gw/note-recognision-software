package model.element.impl

import model.element.api.AbstractElement
import opencv.styles.Color
import org.opencv.core.Mat
import org.opencv.core.Rect

class Note(rect: Rect) : AbstractElement(rect) {
    enum class Name {
        G, F, E, D, C, H, A
    }

    var head: Head? = null
    var name: Name? = null
    var duration: Duration? = null

    enum class Duration(val value: Int) {
        WHOLE(1),
        HALF(2),
        QUARTER(4),
        EIGHT(8)
    }

    override fun getLabel(): String = "Note(${name ?: "?"},${duration?.value ?: "?"})"

    override fun getColor(): Color = Color.GREEN

    override fun drawOn(matrix: Mat) {
        super.drawOn(matrix)
        head?.drawOn(matrix)
    }
}