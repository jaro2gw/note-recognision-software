import nu.pattern.OpenCV
import opencv.image.processor.impl.ToNotes
import org.opencv.imgcodecs.Imgcodecs
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
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
            File(name).deleteRecursively()
            val path = Paths.get(name)
            Files.createDirectory(path)
            val source = Imgcodecs.imread(input)!!
            assert(!source.empty()) { "Could not open image file" }
            val matrices = ToNotes(source)
            matrices.forEachIndexed { index, (operation, mat) ->
                val output = name + "/" + index + "-" + operation.name.toLowerCase() + ".png"
                Imgcodecs.imwrite(output, mat)
            }
            val output = "$name-processed.png"
            Imgcodecs.imwrite(output, matrices.last().second)
            println("File \"$input\" processed.")
        }
}