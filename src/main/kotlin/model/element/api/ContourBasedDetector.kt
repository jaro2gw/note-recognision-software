package model.element.api

import org.opencv.core.Mat
import org.opencv.core.Rect

abstract class ContourBasedDetector<T : AbstractElement> : AbstractDetector<T>() {
    abstract fun Mat.findContours(): Collection<Rect>

    abstract fun Collection<Rect>.convertToElements(): Collection<T>

    override fun Mat.findElements(): Collection<T> = findContours().convertToElements()
}