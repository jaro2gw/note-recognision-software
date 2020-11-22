package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.ImagePreprocessor
import org.opencv.core.Core
import org.opencv.core.Mat
import utils.showInWindow

object ToNegated : ImagePreprocessor {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        Core.bitwise_not(result, result)
        result.showInWindow("Negated")
        return result
    }
}