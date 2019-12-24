package com.andreumargarit.gamecompanion.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class StreamModel(
    val id: String? = null,
    @SerializedName("user_id") val userID: String? = null,
    @SerializedName("user_name") val userName: String?= null,
    @SerializedName("game_id") val gameId: String? = null,
    val type: String?= null,
    val title: String? = null,
    @SerializedName("viewer_count") val viewerCount: Int?= null,
    @SerializedName("started_at") val startedAt: String?= null,
    val language: String?= null,
    @SerializedName("thumbnail_url") var thumbnailUrl: String?= null

):Serializable
{

    var game:GameModel? = null

    fun getSmallThumbnailUrl(): String?
    {
        return thumbnailUrl?.replace("{width}", "300")?.replace("{height}", "300")
    }

    var user: TwitchUserModel?= null
}

data class StreamResponse(
    @SerializedName("data") val results: List<StreamModel>? = null
)