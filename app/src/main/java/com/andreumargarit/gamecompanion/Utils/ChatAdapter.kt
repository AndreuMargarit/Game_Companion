package com.andreumargarit.gamecompanion.Utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.andreumargarit.gamecompanion.Models.ChatMessage
import com.andreumargarit.gamecompanion.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_chat.view.*
import kotlinx.android.synthetic.main.item_chat.view.*
import kotlinx.android.synthetic.main.item_new.view.*

class ChatAdapter (var list: ArrayList<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return list.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup, ChatAdapter: Int): ChatAdapter.ViewHolder {
        var item = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(item)
    }


    override fun onBindViewHolder(holder: ChatAdapter.ViewHolder, position: Int) {
        val chat = list[position]
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        holder.chatText.text = chat.message;
        Picasso.get().load(chat.avatar).into(holder.userImage);
        if(chat.avatar == null)
            holder.userImage.setImageResource(R.drawable.icon_profile)
    }
    class ViewHolder(item: View): RecyclerView.ViewHolder(item) {
        val chatText: TextView = item.chatMessageTextView
        val userImage: ImageView = item.chatAvatarImageView
    }
}

