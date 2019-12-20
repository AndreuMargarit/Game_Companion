package com.andreumargarit.gamecompanion.Models

import com.google.gson.annotations.SerializedName

data class CharacterList(
    var new: ArrayList<CharacterModel>
)

data class CharacterModel(
    val id: Int?= null,
    val name: String?= null,
    @SerializedName("status") val lifeStatus: String?= null,
    val species: String?= null,
    val type: String?= null,
    val origin: CharacterOrigin?= null,
    val location: CharacterLocation?= null,
    val image: String?= null,
    @SerializedName("episode") val episodes: List<String>?= null,
    val url: String?= null,
    val created: String?= null
    )

data class CharacterOrigin(
    val name: String?= null,
    val url: String?= null
)

data class CharacterLocation(
    val name: String?= null,
    val url: String?= null
)

data class CharacterResponse(
    val info: CharacterInfo? = null,
    val results: List<CharacterModel>? = null
)

data class CharacterInfo(
    val count: Int? = null,
    val pages: Int? = null,
    val next: String?= null,
    val prev: String? = null
)