package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
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
            47,
            -2.0
        )
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(8.0, 8.0)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
//        return matrix
        return result
    }
}