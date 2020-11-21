package model.element.api

import org.opencv.core.Mat

abstract class AbstractDetector<T : AbstractElement> {
    abstract fun Mat.preprocessImage(): Mat

    abstract fun Mat.findElements(): Collection<T>

    operator fun invoke(matrix: Mat): Collection<T> = matrix.preprocessImage().findElements()
}