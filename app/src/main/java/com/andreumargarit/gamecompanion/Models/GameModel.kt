package com.andreumargarit.gamecompanion.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GameModel(
    val id: String? = null,
    val name: String? = null,
    @SerializedName("box_art_url") val thumbnailUrl: String? = null
):Serializable

data class GamesResponse(
    @SerializedName("data") val results: List<GameModel>? = null
)