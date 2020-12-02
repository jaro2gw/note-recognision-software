package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Core
import org.opencv.core.Mat

object ToNegated : AbstractImageProcessor() {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        Core.bitwise_not(result, result)
        return result
    }
}