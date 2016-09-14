package com.deanveloper.imagesplitter

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import java.net.URL

/**
 * @author Dean
 */
class ImgurAlbum(val images: List<ImgurImage>) {
    val id: String

    init {
        val result = "https://api.imgur.com/3/album".httpPost(
                listOf(
                        "ids[]" to images.map(ImgurImage::id).reduce { s1, s2 -> "$s1,$s2" },
                        "layout" to "vertical"
                )
        ).addToken().responseString().third

        when(result) {
            is Result.Success -> {
                val json = result.value.toJson().asJsonObject
                id = json["data"].asJsonObject["id"].asString
            }
            is Result.Failure -> {
                throw UploadException(images.toString())
            }
        }
    }
}