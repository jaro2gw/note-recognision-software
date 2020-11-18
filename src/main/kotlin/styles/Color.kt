package styles

import org.opencv.core.Scalar

class Color(r: Double = 0.0, g: Double = 0.0, b: Double = 0.0, a: Double = 0.0) : Scalar(b, g, r, a) {
    companion object {
        val RED = Color(r = 255.0)
        val GREEN = Color(g = 255.0)
        val BLUE = Color(b = 255.0)
        val BLACK = Color()
    }
}