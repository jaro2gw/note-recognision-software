package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import utils.center
import utils.colIterable
import utils.median
import kotlin.math.PI

object ToRotated {
    fun computeRotationAngleRadians(matrix: Mat): Double {
        val edges = Mat()
        Imgproc.Canny(
            matrix,
            edges,
            50.0,
            200.0,
            3,
            false
        )
        val lines = Mat()
        Imgproc.HoughLines(
            edges,
            lines,
            1.0,
            PI / 180,
            150
        )
        return lines.colIterable(0)
            .map { it[1] }
            .median()
    }

    fun byRadiansComputed(matrix: Mat) = object : AbstractImageProcessor() {
        val radians = this@ToRotated.computeRotationAngleRadians(matrix)
        override fun invoke(matrix: Mat): Mat = this@ToRotated.invoke(matrix, radians)
    }

    operator fun invoke(matrix: Mat, radians: Double): Mat {
        val degrees = Math.toDegrees(radians) - 90
        val result = Mat()
        val rotation = Imgproc.getRotationMatrix2D(matrix.center, degrees, 1.0)
        Imgproc.warpAffine(matrix, result, rotation, matrix.size())
        return result
    }
}