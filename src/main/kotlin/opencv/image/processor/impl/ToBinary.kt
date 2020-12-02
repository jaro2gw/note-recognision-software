package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.imgproc.Imgproc

val ToBinary: AbstractImageProcessor = { matrix ->
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
    ToCircles(8.0)(result)
}