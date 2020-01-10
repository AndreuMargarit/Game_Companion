package com.andreumargarit.gamecompanion.Utils

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andreumargarit.gamecompanion.Activities.StreamDetailActivity
import com.andreumargarit.gamecompanion.Models.StreamModel
import com.andreumargarit.gamecompanion.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_stream.view.*

class StreamsAdapter (var list: ArrayList<StreamModel>) : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamsAdapter.ViewHolder {
        var item = LayoutInflater.from(parent.context).inflate(R.layout.item_stream, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: StreamsAdapter.ViewHolder, position: Int) {
        val stream = list[position]

        holder.thumbnail.setOnClickListener {
            val intent = Intent(holder.thumbnail.context, StreamDetailActivity::class.java)
            intent.putExtra("stream", stream)
            holder.thumbnail.context.startActivity(intent) //(MIRAR EN LA MAIN ACTIVITY COMO RECUPERAR ESTOS DATOS)

            Log.i("StreamAdapter", "Clicked in thumbnail")
        }



        holder.title.text = stream.title
        holder.username.text = stream.userName
        Picasso.get().load(stream.thumbnailUrl).into(holder.thumbnail)
        Picasso.get().load(stream.user?.profileImage).into(holder.userLogo)
        holder.viewerCount.text = stream.viewerCount.toString()
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.streamTitleTextView
        val username: TextView = item.streamerUsernameTextView
        val thumbnail: ImageView = item.streamsImageView
        val userLogo: ImageView = item.logoUser
        val viewerCount : TextView = item.viewerCount
    }
}

