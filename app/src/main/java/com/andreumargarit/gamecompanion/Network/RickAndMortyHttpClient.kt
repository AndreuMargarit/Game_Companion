package com.andreumargarit.gamecompanion.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RickAndMortyHttpClient {

    // == static
    companion object
    {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        //Rick And Morty Services
        val endpoints = retrofit.create<RickAndMortyAPI>(RickAndMortyAPI::class.java)
    }
}