package opencv.styles

import org.opencv.core.Scalar

class Color(r: Double = 0.0, g: Double = 0.0, b: Double = 0.0, a: Double = 0.0) : Scalar(b, g, r, a) {
    companion object {
        val RED = Color(r = 255.0)
        val GREEN = Color(g = 255.0)
        val BLUE = Color(b = 255.0)
        val LAWN_GREEN = Color(r = 132.0, g = 222.0, b = 2.0)
        val SEA_GREEN = Color(r = 59.0, g = 122.0, b = 87.0)
        val BLACK = Color()
        val ALMOND = Color(r = 239.0, g = 222.0, b = 205.0)
        val ORANGE = Color(r = 255.0, g = 165.0)
    }
}