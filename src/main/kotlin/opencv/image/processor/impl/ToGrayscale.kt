package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

object ToGrayscale : AbstractImageProcessor() {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        if (result.channels() == 3) Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY)
        return result
    }
}