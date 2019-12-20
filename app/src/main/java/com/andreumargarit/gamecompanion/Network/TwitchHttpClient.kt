package com.andreumargarit.gamecompanion.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TwitchHttpClient {
    companion object{
        private val httpClient = Retrofit.Builder()
            .baseUrl("https://api.twitch.tv/helix/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = httpClient.create(TwitchApi::class.java)
    }
}