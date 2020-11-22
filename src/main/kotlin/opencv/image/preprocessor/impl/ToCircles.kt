package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.ImagePreprocessor
import org.opencv.core.Mat

object ToCircles : ImagePreprocessor {
    override fun invoke(matrix: Mat): Mat = ToHorizontalElements.byWidth(5.0).invoke(matrix)
}