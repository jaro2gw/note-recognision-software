package model

import org.opencv.core.Mat
import org.opencv.core.Rect

open class Element(
    protected open val rectangle: Rect,
    protected open val matrix: Mat,
    protected val type: Type
) {
    enum class Type {
        BAR /*linia*/,
        CLEF /*klucz*/,
        DOT /*kropka*/,
        FLAT /*bemol*/,
        NOTE /*nuta*/,
        NATURAL /*bez znaku*/,
        SHARP /*krzyżyk*/,
        PAUSE /*pauza*/
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Element

        if (rectangle != other.rectangle) return false
        if (matrix != other.matrix) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rectangle.hashCode()
        result = 31 * result + matrix.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString(): String {
        return "Element(rectangle=$rectangle, matrix=$matrix, type=$type)"
    }
}