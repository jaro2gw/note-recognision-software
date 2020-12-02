package opencv.image.processor.impl

import opencv.image.processor.api.AbstractImageProcessor
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import utils.center
import utils.colIterable
import utils.median
import kotlin.math.PI

object ToRotated {
    private fun computeRotationAngleRadians(matrix: Mat): Double {
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

    private fun byRadians(radians: Double): AbstractImageProcessor = { matrix ->
        val degrees = Math.toDegrees(radians) - 90
        val result = Mat()
        val rotation = Imgproc.getRotationMatrix2D(matrix.center, degrees, 1.0)
        Imgproc.warpAffine(matrix, result, rotation, matrix.size())
        result
    }

    operator fun invoke(matrix: Mat): AbstractImageProcessor {
        val radians = computeRotationAngleRadians(matrix)
        return byRadians(radians)
    }
}