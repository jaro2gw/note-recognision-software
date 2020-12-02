package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat

object ToCircles : AbstractImageProcessor() {
    override fun invoke(matrix: Mat): Mat = ToHorizontalElements.byWidth(10.0).invoke(matrix)
}