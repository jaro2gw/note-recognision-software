package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.imgproc.Imgproc

object ToVerticalElements {
    operator fun invoke(height: Double): AbstractImageProcessor = ToKernel(Imgproc.MORPH_RECT, 1.0, height)
}