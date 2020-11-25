package opencv.image.preprocessor.impl

import opencv.image.preprocessor.api.AbstractImagePreprocessor
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import utils.showInWindow

object ToHorizontalElements {
    operator fun invoke(matrix: Mat, width: Double): Mat {
        val result = matrix.clone()
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(width, 1.0)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
        result.showInWindow("Horizontal Elements")
        return result
    }

    fun byWidth(width: Double): AbstractImagePreprocessor = object : AbstractImagePreprocessor() {
        override fun invoke(matrix: Mat): Mat = this@ToHorizontalElements.invoke(matrix, width)
    }
}