package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.ImagePreprocessor
import org.opencv.core.Mat

class CompositeImagePreprocessor(private vararg val preprocessors: ImagePreprocessor) : ImagePreprocessor {
    fun results(matrix: Mat): List<Mat> =
        preprocessors.runningFold(matrix) { acc, preprocessor -> preprocessor(acc) }

    override fun invoke(matrix: Mat): Mat = results(matrix).last()
}