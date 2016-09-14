package com.deanveloper.imagesplitter

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*
import javax.imageio.ImageIO

/**
 * @author Dean
 */
class ImgurImage(val image: BufferedImage, val name: String) {
    val id: String

    init {
        val baos = ByteArrayOutputStream()
        ImageIO.write(image, "png", baos)
        baos.flush()
        val result = "https://api.imgur.com/3/upload".httpPost(
                listOf(
                        "image" to Base64.getEncoder().encode(baos.toByteArray()),
                        "type" to "base64",
                        "name" to name
                )
        ).addToken().responseString().third

        when(result) {
            is Result.Success -> {
                val json = result.value.toJson().asJsonObject
                id = json["data"].asJsonObject["id"].asString
            }
            is Result.Failure -> {
                throw UploadException(name)
            }
        }
    }

    override fun toString() = id

    override fun equals(other: Any?) = id == other

    override fun hashCode() = id.hashCode()
}

class UploadException(msg: String) : RuntimeException(msg)