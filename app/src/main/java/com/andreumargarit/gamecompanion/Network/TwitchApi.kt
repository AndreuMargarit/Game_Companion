package com.andreumargarit.gamecompanion.Network

import com.andreumargarit.gamecompanion.Models.GamesResponse
import com.andreumargarit.gamecompanion.Models.StreamResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchApi{

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("streams")
    suspend fun getStreams(@Query("game_id") gameId: String? = null): StreamResponse

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("streams")
    suspend fun getGames(@Query("id") gameIds: List<String>? = null): GamesResponse

}