import nu.pattern.OpenCV
import opencv.ImageProcessor
import java.io.File


fun main(args: Array<String>) {
    OpenCV.loadShared()
    val fileNameRegex = Regex(".+\\.png")
    args.asSequence()
        .filter {
            val valid = fileNameRegex.matches(it)
            if (!valid) println("File name \"$it\" is not a valid png file name. It will be skipped.")
            return@filter valid
        }
        .filter {
            val exists = File(it).exists()
            if (!exists) println("File \"$it\" does not exist. It will be skipped.")
            return@filter exists
        }
        .map { input ->
            val name = input.dropLast(4)
            val output = "$name.processed.png"
            input to output
        }
        .forEach { (input, output) -> ImageProcessor(input, output) }
}