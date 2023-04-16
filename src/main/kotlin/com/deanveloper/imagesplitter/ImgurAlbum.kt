package com.deanveloper.imagesplitter

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

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

        when (result) {
            is Result.Success -> {
                val json = result.value.toJson().asJsonObject
                id = json["data"].asJsonObject["id"].asString
            }
            is Result.Failure -> {
                throw UploadException(images.toString())
            }
        }
    }

    fun publish(title: String, topic: String = "funny", mature: Boolean) {
        while (true) {
            "https://api.imgur.com/3/gallery/album/$id".httpPost(
                    listOf(
                            "title" to title,
                            "topic" to topic,
                            "mature" to if(mature) 1 else 0
                            )
            ).addToken()
        }
    }
}