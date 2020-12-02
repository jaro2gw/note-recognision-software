package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

object ToKernel {
    operator fun invoke(shape: Int, width: Double, height: Double): AbstractImageProcessor = { matrix ->
        val result = matrix.clone()
        val kernel = Imgproc.getStructuringElement(
            shape,
            Size(width, height)
        )
        Imgproc.erode(result, result, kernel)
        Imgproc.dilate(result, result, kernel)
        result
    }
}