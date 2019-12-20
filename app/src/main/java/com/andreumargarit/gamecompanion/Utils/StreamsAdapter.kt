package com.andreumargarit.gamecompanion.Utils

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andreumargarit.gamecompanion.Models.StreamModel
import com.andreumargarit.gamecompanion.R
import kotlinx.android.synthetic.main.item_new.view.*

class StreamsAdapter (var list: ArrayList<StreamModel>) : RecyclerView.Adapter<StreamsAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamsAdapter.ViewHolder {
        var item = LayoutInflater.from(parent.context).inflate(R.layout.item_new, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: StreamsAdapter.ViewHolder, position: Int) {
        val stream = list[position]

        holder.itemView.setOnClickListener {
            //val intent = Intent(holder.elementoUI.context, StreamDetailActivity::class.java)
            //intent.putExtra("stream", stream)
            //holder.elementoUI.context.startActivity(intent)(MIRAR EN LA MAIN ACTIVITY COMO RECUPERAR ESTOS DATOS)
        }

    }
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val newsText: TextView = item.newsTextView
        val imageView: ImageView = item.newsImageView
    }
}

