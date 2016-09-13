package com.deanveloper.imagesplitter

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.system.exitProcess

/**
 * @author Dean
 */
fun main(args: Array<String>) {
    try {
        if(args.size !== 3) {
            throw ExpectedException("There should be 3 arguments: filename, height, output")
        }

        val file = File(args[0])
        if (!file.exists()) {
            throw ExpectedException("The file specified in the first argument does not exist! (recieved: ${args[1]})")
        }

        val outputFileName = File(args[2]).nameWithoutExtension

        val height: Int
        try {
            height = args[1].toInt()
        } catch (e: NumberFormatException) {
            throw ExpectedException("The second argument must be an integer! (recieved: ${args[1]})")
        }

        val input: BufferedImage = ImageIO.read(file)
        val output = mutableListOf<BufferedImage>()

        for(i in 0 .. input.height step height) {
            output.add(input.getSubimage(0, i, input.width, Math.min(input.height - i, height)))
        }

        var index = 0
        for(image in output) {
            val outputFile = File("${outputFileName}_${index}.png")
            if(outputFile.exists()) {
                println("${outputFile.name} already exists! Overwriting!")
            }
            ImageIO.write(image, "png", outputFile)
            index++
        }
    } catch (e: ExpectedException) {
        println(e.message)
        exitProcess(1)
    } catch (e: Throwable) {
        println("An exception occurred, send this to the developer at " +
                "https://github.com/Deanveloper/ImageSplitter/issues")

        e.printStackTrace()
    }
}

class ExpectedException(message: String) : Exception(message)