package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.ImagePreprocessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import utils.showInWindow

object ToBinary : ImagePreprocessor {
    override fun invoke(matrix: Mat): Mat {
        val result = Mat()
        Imgproc.adaptiveThreshold(
            matrix,
            result,
            255.0,
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            15,
            -2.0
        )
        result.showInWindow("Binary")
        return result
    }
}