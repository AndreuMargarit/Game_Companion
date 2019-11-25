package com.andreumargarit.gamecompanion.Models

data class UserModel (
    val userName : String? = null,
    val email : String? =null,
    val userID : String? = null,

    val avatarUrl : String? = null,
    val followers: Int? = 0)
{

};