package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.ImagePreprocessor
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import utils.showInWindow

object ToVerticalElements : ImagePreprocessor {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(1.0, 5.0)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
        result.showInWindow("Vertical Elements")
        return result
    }
}