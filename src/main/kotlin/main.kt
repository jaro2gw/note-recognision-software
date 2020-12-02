import nu.pattern.OpenCV
import opencv.image.processor.impl.ToNotes
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main(args: Array<String>) {
    val debug = args.contains("--debug")
    OpenCV.loadShared()
    println("Reading file names to process from stdin, one file per line.")
    generateSequence { readLine() }
        .filter { input ->
            val exists = File(input).exists()
            if (!exists) println("File \"$input\" does not exist. It will be skipped.")
            return@filter exists
        }
        .forEach { input ->
            val name = input.dropLast(4)
            if (debug) {
                val path = Paths.get(name)
                if (Files.notExists(path)) Files.createDirectory(path)
            }
            val source = Imgcodecs.imread(input)!!
            assert(!source.empty()) { "Could not open image file" }
            val matrices = ToNotes(source)
            if (debug) matrices.forEachIndexed { index, (operation, mat) ->
                val output = name + "/" + index + "-" + operation.name.toLowerCase() + ".png"
                Imgcodecs.imwrite(output, mat)
            }
            val output = "$name-processed.png"
            Imgcodecs.imwrite(output, matrices.last().second)
            println("File \"$input\" processed.")
        }
}