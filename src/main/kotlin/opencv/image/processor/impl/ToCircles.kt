package opencv.image.processor.impl

import org.opencv.imgproc.Imgproc

object ToCircles {
    operator fun invoke(radius: Double) = ToKernel(Imgproc.MORPH_ELLIPSE, radius, radius)
}