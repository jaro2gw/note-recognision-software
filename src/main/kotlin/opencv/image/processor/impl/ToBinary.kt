package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

object ToBinary : AbstractImageProcessor() {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        Imgproc.adaptiveThreshold(
            result,
            result,
            255.0,
            Imgproc.ADAPTIVE_THRESH_MEAN_C,
            Imgproc.THRESH_BINARY,
            15,
            -2.0
        )
        return matrix
//        return result
    }
}