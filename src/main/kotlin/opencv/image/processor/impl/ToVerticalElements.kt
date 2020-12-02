package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object ToVerticalElements : AbstractImageProcessor() {
    override fun invoke(matrix: Mat): Mat {
        val result = matrix.clone()
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(1.0, 15.0)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
        return result
    }
}