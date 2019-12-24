package com.andreumargarit.gamecompanion.Network

import com.andreumargarit.gamecompanion.Models.GamesResponse
import com.andreumargarit.gamecompanion.Models.StreamResponse
import com.andreumargarit.gamecompanion.Models.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface TwitchApi{

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("streams")
    suspend fun getStreams(): StreamResponse

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("games")
    suspend fun getGames(@Query("id") gameIds: List<String>? = null): GamesResponse

    @Headers("Client-ID: ywvglt0gib8rqdly0ejobehqfi071m")
    @GET("users")
    suspend fun getUsers(@Query("id") userID: List<String>? = null) : UserResponse
}