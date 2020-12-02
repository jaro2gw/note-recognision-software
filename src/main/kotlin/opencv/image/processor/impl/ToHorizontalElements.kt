package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object ToHorizontalElements {
    operator fun invoke(matrix: Mat, width: Double): Mat {
        val result = matrix.clone()
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(width, 1.0)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
        return result
    }

    fun byWidth(width: Double): AbstractImageProcessor = object : AbstractImageProcessor() {
        override fun invoke(matrix: Mat): Mat = this@ToHorizontalElements.invoke(matrix, width)
    }
}