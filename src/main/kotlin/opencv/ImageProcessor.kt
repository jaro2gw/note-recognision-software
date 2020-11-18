package opencv

import org.opencv.imgcodecs.Imgcodecs

class ImageProcessor(val inputFileName: String, val outputFileName: String) {
    val input = Imgcodecs.imread(inputFileName)!!
    val output = input.clone()!!

    operator fun invoke() {
        //TODO do magic here

        Imgcodecs.imwrite(outputFileName, output)
    }
}