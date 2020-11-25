package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.AbstractImagePreprocessor
import org.opencv.core.Mat

object ToCircles : AbstractImagePreprocessor() {
    override fun invoke(matrix: Mat): Mat = ToHorizontalElements.byWidth(10.0).invoke(matrix)
}