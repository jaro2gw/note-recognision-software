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
    val matrix = clone()!!
    if (matrix.channels() < 3) {
        Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_GRAY2BGR)
    }
    return matrix
}

fun Mat.toGray(): Mat {
    val matrix = clone()!!
    if (matrix.channels() == 3) {
        Imgproc.cvtColor(matrix, matrix, Imgproc.COLOR_BGR2GRAY)
    }
    return matrix
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

fun Rect.prettyString() = "[x=$x,\ty=$y,\tw=$width,\th=$height]"

operator fun Point.component1() = x
operator fun Point.component2() = y

fun Mat.detect(template: Template): List<Rect> {
    val templateMatrix = Imgcodecs.imread(template.fileName())
        .toGray()
        .convertTo(CvType.CV_32FC1)

    val source = clone()
        .convertTo(CvType.CV_32FC1)

    val cols = source.cols() - templateMatrix.cols() + 1
    val rows = source.rows() - templateMatrix.cols() + 1

    val result = Mat().apply {
        create(rows, cols, CvType.CV_32FC1)
    }

    val threshold = 0.9

    Imgproc.matchTemplate(source, templateMatrix, result, Imgproc.TM_CCOEFF_NORMED)
    Imgproc.threshold(result, result, threshold, 255.0, Imgproc.THRESH_TOZERO)

    val points = mutableListOf<Point>()
    while (true) {
        val loc = Core.minMaxLoc(result) ?: break
        if (loc.maxVal > threshold) {
            points.add(loc.maxLoc)
            val x = loc.maxLoc.x.toInt()
            val y = loc.maxLoc.y.toInt()
            Imgproc.floodFill(result, Mat(), loc.maxLoc, Scalar(0.0))
            result.put(x, y, 0.0)
        } else break
    }

    return points.map { (x, y) ->
        Rect(x.toInt(), y.toInt(), templateMatrix.width(), templateMatrix.height())
    }
}

fun Mat.convertTo(rtype: Int) = apply { convertTo(this, rtype) }

fun Mat.matchTemplate(template: Template, threshold: Double): Pair<Mat, Mat> {
    val templateMatrix = Imgcodecs.imread(Template.NOTE_HEAD.fileName())!!
    return templateMatrix to matchTemplate(templateMatrix, threshold)
}

fun Mat.matchTemplate(template: Mat, threshold: Double): Mat {
    val source = clone().convertTo(CvType.CV_32FC1)

    val templateMatrix = template
        .toGray()
        .convertTo(CvType.CV_32FC1)

    val result = Mat().apply {
        create(source.width(), source.height(), CvType.CV_32FC1)
    }

    Imgproc.matchTemplate(source, templateMatrix, result, Imgproc.TM_CCOEFF_NORMED)
    Imgproc.threshold(result, result, threshold, 255.0, Imgproc.THRESH_TOZERO)

    return result
}

fun Mat.classify(template: Template, rectangles: List<Rect>): Map<Rect, Mat> {
    val templateMatrix = Imgcodecs.imread(template.fileName())!!
        .toGray()
        .convertTo(CvType.CV_32FC1)

    val result = Mat().apply {
        create(templateMatrix.width(), templateMatrix.height(), CvType.CV_32FC1)
    }

    val threshold = 0.9
    val elements = mutableMapOf<Rect, Mat>()

    for (rectangle in rectangles) {
        val matrix = crop(rectangle).convertTo(CvType.CV_32FC1)
        Imgproc.matchTemplate(templateMatrix, matrix, result, Imgproc.TM_CCOEFF_NORMED)
        Imgproc.threshold(result, result, threshold, 255.0, Imgproc.THRESH_TOZERO)
        val loc = Core.minMaxLoc(result)
        if (loc.maxVal > threshold) {
            elements[rectangle] = matrix
        }
    }

    return elements
}

fun Mat.approximateNoteHeadCenter(): Point? {
    val threshold = 0.9
    val (templateMatrix, result) = matchTemplate(Template.NOTE_HEAD, threshold)
    val loc = Core.minMaxLoc(result) ?: return null
    if (loc.maxVal > threshold) {
        val point = loc.maxLoc ?: return null
        return Point(
            point.x + templateMatrix.width() / 2,
            point.y + templateMatrix.height() / 2
        )
    }
    return null
}
