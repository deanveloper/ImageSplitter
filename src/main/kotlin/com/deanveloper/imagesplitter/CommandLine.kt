package com.deanveloper.imagesplitter

import com.github.kittinunf.fuel.core.Request
import com.google.gson.JsonParser
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.system.exitProcess

/**
 * @author Dean
 */
val PARSER = JsonParser()

fun main(args: Array<String>) {
    try {
        val (file, height, outputArg) = parseArgs(args)

        val input: BufferedImage = ImageIO.read(file)
        val output = mutableListOf<BufferedImage>()

        for (i in 0..input.height step height) {
            output.add(input.getSubimage(0, i, input.width, Math.min(input.height - i, height)))
        }

        if (outputArg == "upload") {

            if (Tokens.load()) {
                Tokens.getViaOAuth()
                Tokens.save()
            }

        } else {
            val outputFileName = File(args[2]).nameWithoutExtension

            var index = 0
            for (image in output) {
                val outputFile = File(String.format("%s_%3d.png", outputFileName, index))
                if (outputFile.exists()) {
                    println("${outputFile.name} already exists! Overwriting!")
                }
                ImageIO.write(image, "png", outputFile)
                index++
            }
        }
    } catch (e: SyntaxException) {
        println(e.message)
        println("Usage: java -jar ImageSplitterApp.jar <filename> <height> <outputFile|upload>")
        exitProcess(1)
    } catch (e: Throwable) {
        println("An exception occurred, send this to the developer at " +
                "https://github.com/Deanveloper/ImageSplitterApp/issues")

        e.printStackTrace()
    }
}

fun parseArgs(args: Array<String>): Triple<File, Int, String> {
    if (args.size < 3) {
        throw SyntaxException("There should be 3 arguments: filename, height, output")
    }

    val file = File(args[0])
    if (!file.exists()) {
        throw SyntaxException("The file specified in the first argument does not exist! (recieved: ${args[1]})")
    }

    val height: Int
    try {
        height = args[1].toInt()
    } catch (e: NumberFormatException) {
        throw SyntaxException("The second argument must be an integer! (recieved: ${args[1]})")
    }

    return Triple(file, height, args[2])
}

tailrec fun tryAgainPrompt() {
    println("Try again? (Y/N)")
    val input = readLine()

    if (input?.equals("Y", true) ?: false) {
        return
    }

    if (input?.equals("N", true) ?: false) {
        exitProcess(1)
    }

    println("Not a valid input!")
    tryAgainPrompt()
}

fun String.toJson() = PARSER.parse(this)

fun Request.addToken() = this.header("Authorization" to "Bearer ${Tokens.access}")

val whyNot: String
    get() {
        var lmao = "VmpJd2VGVXlSbkpOVldoWFlsaENhRlZyV2t0a1JtUjBZMFZ3VDFaVVJrWldWekI0VkcxV2RHRkhPVlZpUjFKb1dWZDRkMUpX" +
                "VmxWU2F6VlhUVmhDZDFZeWVGTmhiVkY0WTBWV1ZGWkZjRTlhVjNSWFRsWmtkR05HV2s5V1ZHeFRWVVpSZDFCUlBUMD0="
        for (i in 0 until 5) {
            lmao = String(Base64.getDecoder().decode(lmao))
        }
        return lmao
    }

class SyntaxException(message: String) : Exception(message)