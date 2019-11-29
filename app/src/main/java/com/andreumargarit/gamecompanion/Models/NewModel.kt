package com.andreumargarit.gamecompanion.Models

data class NewsList(
    var new: ArrayList<NewModel>?
)

data class NewModel (
    var text: String?,
    var imageUrl:String? = null
)