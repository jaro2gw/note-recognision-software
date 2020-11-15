package opencv.utils

import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc

fun Mat.crop(rectangle: Rect) = submat(
    rectangle.y,
    rectangle.y + rectangle.height,
    rectangle.x,
    rectangle.x + rectangle.width
)

fun Mat.binary(
    blockSize: Int = 15,
    constant: Double = -2.0
): Mat { //TODO CHECK Mat binary = new Mat(src.rows(), src.cols(), src.type(), new Scalar(0));
    val binary = Mat()
    Imgproc.adaptiveThreshold(
        this,
        binary,
        255.0,
        Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
        Imgproc.THRESH_BINARY,
        blockSize,
        constant
    )
    return binary
}

fun Mat.horizontalObjects(): Mat {
    val objects = clone()!!
    val size = objects.cols() / 30.0
    val structure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(size, 1.0))
    Imgproc.erode(objects, objects, structure)
    Imgproc.dilate(objects, objects, structure)
    return objects
}

fun Mat.verticalObjects(): Mat {
    val objects = clone()!!
    val size = objects.rows() / 30.0
    val structure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(1.0, size))
    Imgproc.erode(objects, objects, structure)
    Imgproc.dilate(objects, objects, structure)
    return objects
}

fun Mat.refinedObjects(): Mat {
    val refinedObjects = clone()!!
    val edges = refinedObjects.binary(3)

    val size = Size(2.0, 2.0)
    val kernel = Mat.ones(size, CvType.CV_8UC1)
    Imgproc.dilate(edges, edges, kernel)

    val smooth = Mat()
    refinedObjects.copyTo(smooth)
    Imgproc.blur(smooth, smooth, size)
    smooth.copyTo(refinedObjects, smooth)

    Core.bitwise_not(refinedObjects, refinedObjects)
    return refinedObjects
}

fun Mat.save(file: String) {
    Imgcodecs.imwrite(file, this)
}

fun Point.insideRect(rectangle: Rect): Boolean {
    return this.x >= rectangle.x &&
            this.y >= rectangle.y &&
            this.x <= (rectangle.x + rectangle.width) &&
            this.y <= (rectangle.y + rectangle.height)
}

fun Mat.elementCountourRectangles(): List<Rect> {
    val input = clone()!!.binary(11, 2.0).inv()!!
    val contours = emptyList<MatOfPoint>()
    Imgproc.findContours(
        input,
        contours,
        Mat(),
        Imgproc.RETR_LIST,
        Imgproc.CHAIN_APPROX_SIMPLE
    )
    return contours.map(Imgproc::boundingRect)/*.generify()*/
}

operator fun Rect.compareTo(other: Rect): Int =
    if (x == other.x) y.compareTo(other.y)
    else x.compareTo(other.x)
//    val (first, second) = if (abs(y - other.y) < 50) {
//        x to other.x
//    } else {
//        y to other.y
//    }
//    return first.compareTo(second)


//fun List<Rect>.generify(): List<Rect> {
//
//}

fun Mat.toBGR(): Mat {
    if (channels() < 3) {
        Imgproc.cvtColor(this, this, Imgproc.COLOR_GRAY2BGR)
    }
    return this
}

fun Mat.drawContours(rectangles: List<Rect>, color: Scalar): Mat {
    val input = clone()!!.toBGR()
    rectangles.forEach { rectangle ->
        Imgproc.rectangle(
            input,
            rectangle,
            color,
            1,
            Imgproc.LINE_8,
            0
        )
    }
    return input
}