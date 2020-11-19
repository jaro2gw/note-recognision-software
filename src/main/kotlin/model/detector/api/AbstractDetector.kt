package model.detector.api

import model.element.api.AbstractElement
import org.opencv.core.Mat
import org.opencv.core.Rect

abstract class AbstractDetector<T : AbstractElement> {
    abstract fun Mat.preprocessImage(): Mat

    abstract fun Mat.findElementContours(): Collection<Rect>

    abstract fun Collection<Rect>.convertToElements(): Collection<T>

    operator fun invoke(matrix: Mat): Collection<T> = matrix.preprocessImage()
        .findElementContours()
        .convertToElements()
}