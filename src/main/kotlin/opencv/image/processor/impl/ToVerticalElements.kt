package opencv.image.processor.impl

import org.opencv.imgproc.Imgproc

object ToVerticalElements {
    operator fun invoke(height: Double) = ToKernel(Imgproc.MORPH_RECT, 1.0, height)
}