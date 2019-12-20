package com.andreumargarit.gamecompanion.Network

import com.andreumargarit.gamecompanion.Models.CharacterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface RickAndMortyAPI {

    @GET("character")
    fun getCharacters(): Call<CharacterResponse>
}