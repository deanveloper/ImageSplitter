package com.deanveloper.imagesplitter

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import com.google.gson.JsonParser
import java.io.File

/**
 * @author Dean
 */
object Tokens {
    var access: String = ""
        private set
    var refresh: String = ""
        private set

    /**
     * Saves tokens to file
     */
    fun save() {
        val f = File("apicodes.txt")
        if(f.exists()) {
            println("apicodes.txt already exists! Overwriting...")
        }
        f.writeText("$access\n$refresh")
    }

    /**
     * Loads tokens from file
     *
     * @return if successful
     */
    fun load(): Boolean {
        val file = File("token.txt")
        if (file.exists()) {
            val access = file.readLines().getOrNull(0)
            val refresh = file.readLines().getOrNull(1)
            if(access.isNullOrBlank() || refresh.isNullOrBlank()) {
                return false
            }
            this.access = access!!
            this.refresh = refresh!!
            return true
        } else {
            return false
        }
    }

    /**
     * Refresh the access token
     */
    fun getNewAccessToken() {
        throw UnsupportedOperationException("Not supported yet")
    }

    fun getViaOAuth() {
        while (true) {
            val pin = readLine()
            if (pin != null) {
                val state = Math.random().toString()
                val result = "https://api.imgur.com/oauth2/token".httpPost(
                        listOf(
                                "client_id" to "e96bbfb9b380cc7",
                                "client_secret" to whyNot,
                                "pin" to pin,
                                "state" to state
                        )
                ).responseString().third

                when (result) {
                    is Result.Success -> {
                        val json = JsonParser().parse(result.value).asJsonObject
                        val access = json["access_token"].asString
                        val refresh = json["refresh_token"].asString

                        this.access = access
                        this.refresh = refresh
                    }
                    is Result.Failure -> {
                        println("Error: " + result.error.message)

                        tryAgainPrompt()
                    }
                }
            }
        }
    }
}