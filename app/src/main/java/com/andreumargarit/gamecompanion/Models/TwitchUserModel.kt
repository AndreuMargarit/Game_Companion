package com.andreumargarit.gamecompanion.Models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TwitchUserModel (
    val id: String?= null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("profile_image_url") val profileImage: String?= null,
    @SerializedName("view_count") val viewCount: Int?= null,
    val description: String?= null
): Serializable

data class UserResponse(
    @SerializedName("data") val results: List<TwitchUserModel>? = null
)