package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.imgproc.Imgproc

val ToGrayscale: AbstractImageProcessor = { matrix ->
    val result = matrix.clone()
    if (result.channels() == 3) Imgproc.cvtColor(result, result, Imgproc.COLOR_BGR2GRAY)
    result
}