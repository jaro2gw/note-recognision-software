package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.AbstractImagePreprocessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import utils.showInWindow

object ToGrayscale : AbstractImagePreprocessor() {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        if (result.channels() == 3) Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY)
        result.showInWindow("Grayscale")
        return result
    }
}