package com.andreumargarit.gamecompanion.Models

data class NewsList(
    var new: ArrayList<NewModel>?
)

data class NewModel (
    var title: String? = null,
    var imageUrl:String? = null
)