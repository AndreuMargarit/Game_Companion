package com.andreumargarit.gamecompanion.Models

data class ChatMessage(
    val text: String ?= null,
    val timestamp: Long?= null,
    val username: String? = null,
    val avatar: String? = null
)