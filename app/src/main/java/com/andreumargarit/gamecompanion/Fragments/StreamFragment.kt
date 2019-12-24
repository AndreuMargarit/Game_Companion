package com.andreumargarit.gamecompanion.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreumargarit.gamecompanion.Models.*
import com.andreumargarit.gamecompanion.Network.RickAndMortyHttpClient
import com.andreumargarit.gamecompanion.Network.TwitchHttpClient

import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.StreamsAdapter
import kotlinx.android.synthetic.main.fragment_stream.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class StreamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream, container, false)
    }

    //val adapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStreams()
    }
    private fun getStreams() {
        Log.i("StreamFragment", "START_STREAMS")
        lifecycleScope.launch{
        try {
            val streamResponse = TwitchHttpClient.service.getStreams()
            var streams = streamResponse.results ?: emptyList()
            getGamesForStreams(streams)
            SetThumbnailsSize(streams)
            getUsersForStreams(streams)
            Log.i("StreamFragment", "Got ${streams.count()} streams")
            Log.i("StreamFragment", "Got streams with games ${streams.map{it.game}}")
            streamsRecyclerView.adapter = StreamsAdapter(ArrayList(streams.orEmpty()))
            streamsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        } catch(error: IOException){
            //No response
            Log.w("StreamFragment", "No internet")
        } catch (error: HttpException)
        {
            Log.w("StreamFragment", error.message())
        }
    }


    /*TwitchHttpClient.service.getStreams(null).enqueue(object : Callback<StreamResponse>{
        override fun onFailure(call: Call<StreamResponse>, t: Throwable)
        {
            Log.w("StreamFragment", t.localizedMessage)
        }

        override fun onResponse(
            call: Call<StreamResponse>,
            response: Response<StreamResponse>
        ) {
            if(response.isSuccessful){
                val streams = response.body()?.results ?: emptyList()
                Log.i("StreamFragment", streams.toString())

                getGamesForStreams(streams)

            }
            else
            {
                Log.w("StreamFragment", response.errorBody()?.string() ?: "null error body")
            }
        }
    })

     */
    }

    private fun SetThumbnailsSize(streams: List<StreamModel>) : List<StreamModel>{
        streams.forEach { stream->
            stream.thumbnailUrl =  stream.thumbnailUrl?.replace("{width}", "300")?.replace("{height}", "300")
        }
        return streams
    }

    private suspend fun getGamesForStreams(streams: List<StreamModel>): List<StreamModel> {
        val ids = streams.map {
            return@map it.gameId ?: ""
        }

        val games = TwitchHttpClient.service.getGames(gameIds = ids).results ?: emptyList<GameModel>()

        streams.forEach { stream-> games.forEach { game->
            if(stream.gameId == game.id)
                stream.game = game;
            }
        }
        return streams
        /*
        TwitchHttpClient.service.getGames(ids).enqueue(object: Callback<GamesResponse>
        {
            override fun onFailure(call: Call<GamesResponse>, t: Throwable) {
                Log.w("StreamsFragment", t.localizedMessage ?: "null error body")
            }

            override fun onResponse(call: Call<GamesResponse>, response: Response<GamesResponse>) {
                if(response.isSuccessful){
                    val games = response.body()?.results ?: emptyList()
                    Log.i("StreamFragment", games.toString())
                    //Merge Games to Strams
                    streams.forEach { stream-> streams.forEach { stream-> stream.game = games.firstOrNull { stream.gameId == it.id} } }

                    //Update Adapter
                    //adapter.list = ArrayList(streams)
                    //adapter.notifyDataSetChanged() //Refresh recyclerView
                }
            }
        })

 */
    }

    private suspend fun getUsersForStreams(streams: List<StreamModel>): List<StreamModel>
    {
        val ids = streams.map {
            return@map it.userID ?: ""
        }

        val users = TwitchHttpClient.service.getUsers(userID = ids).results ?: emptyList<TwitchUserModel>()

        streams.forEach { stream-> users.forEach { user->
            if(stream.userID == user.id)
                stream.user = user;
            }
        }
        return streams
    }


    fun GetRickAndMortyFunction() {
        val characters = RickAndMortyHttpClient.endpoints.getCharacters()
            .enqueue(object : Callback<CharacterResponse> {
                override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                    Log.w("StreamFragment", t.localizedMessage)
                }

                override fun onResponse(
                    call: Call<CharacterResponse>,
                    response: Response<CharacterResponse>
                ) {
                    Log.i("StreamFragment", "conectats a willy")
                    if (response.code() == 200) {
                        Log.i("StreamFragment", "Todo OK Jose Luís")
                        Log.i(
                            "StreamFragment",
                            response.body()?.results?.toString() ?: "Empty body"
                        )
                    } else {
                        Log.w("StreamFragment", response.errorBody()?.string() ?: "Null error body")
                    }
                }
            })
    }
}
