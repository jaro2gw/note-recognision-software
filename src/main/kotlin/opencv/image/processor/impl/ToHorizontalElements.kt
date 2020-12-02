package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.imgproc.Imgproc

object ToHorizontalElements {
    operator fun invoke(width: Double): AbstractImageProcessor = ToKernel(Imgproc.MORPH_RECT, width, 1.0)
}