package opencv

import model.Stave
import org.opencv.core.Mat
import org.opencv.core.Rect

class StaveDetector {
    /**
     * @param matrix a binary image matrix with horizontal lines
     * @return a list of rectangles with
     */
    fun find(matrix: Mat): List<Rect> {
        return emptyList()
    }

    fun convert(rectangles: List<Rect>): List<Stave> {
        return emptyList()
    }
}