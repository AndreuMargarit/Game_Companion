package com.andreumargarit.gamecompanion.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andreumargarit.gamecompanion.Models.NewModel
import com.andreumargarit.gamecompanion.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_new.view.*

class NewsAdapter (var list: ArrayList<NewModel>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>(){

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        var item = LayoutInflater.from(parent.context).inflate(R.layout.item_new, parent, false)
        return ViewHolder(item)
    }


    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        val new = list[position]

        //val stream = list[position]

        //STREAM ADAPTER (TODO)
        //holder.itemView.setOnClickListener{
        //val intent = Intent(holder.elementoUI.context, StreamDetailActivity::class.java)
        //intent.putExtra("stream", stream)
        //holder.elementoUI.context.startActivity(intent)  (MIRAR EN LA MAIN ACTIVITY COMO RECUPERAR ESTOS DATOS)
        //}

        holder.newsText.text = new.title;
        Picasso.get().load(new.imageUrl).into(holder.imageView);
    }

    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val newsText: TextView = item.newsTextView
        val imageView: ImageView = item.newsImageView
    }
}