package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.imgproc.Imgproc

object ToCircles {
    operator fun invoke(radius: Double): AbstractImageProcessor = ToKernel(Imgproc.MORPH_ELLIPSE, radius, radius)
}