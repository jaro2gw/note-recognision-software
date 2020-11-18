import nu.pattern.OpenCV
import java.io.File


fun main(args: Array<String>) {
    OpenCV.loadShared()

    val sequence =
        if (args.isEmpty()) {
            println("Reading from stdin")
            generateSequence(::readLine)
        } else {
            println("Reading from program arguments")
            args.asSequence()
        }

    val fileNameRegex = Regex(".+\\.png")

    sequence
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
            val output = "$name-processed.png"
            input to output
        }
        .map { (input, output) ->
            StaveImageProcessor(input, output)
        }
        .forEach {
            it.invoke()
        }
}