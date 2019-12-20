package com.andreumargarit.gamecompanion.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreumargarit.gamecompanion.Models.ChatMessage
import com.andreumargarit.gamecompanion.Models.NewModel

import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.ChatAdapter
import com.andreumargarit.gamecompanion.Utils.Constants
import com.andreumargarit.gamecompanion.Utils.NewsAdapter
import com.andreumargarit.gamecompanion.Utils.UserDao
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_news.*

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    private val adapter = ChatAdapter(arrayListOf())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sendButton.setOnClickListener {
        sendMessage(sendButton.text.toString())} //TODO: fer un editText on posar el missatge i enviar aixo, no el sendButton.text

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter;

        subscribeToMessageChanges()
    }

    override fun onResume() {
        super.onResume()
    }

    private  fun sendMessage(text: String)
    {
        val message = ChatMessage(text = text);
        FirebaseFirestore.getInstance()
            .collection("chat")
            .add(message)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

    }

    private fun subscribeToMessageChanges(){
        FirebaseFirestore.getInstance()
            .collection("chat")
            .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
                //Query Snapshot contains documents & metadata
                querySnapshot?.let {

                    val messages = querySnapshot.toObjects(ChatMessage::class.java) ?: emptyList()
                    adapter.list = ArrayList(messages)
                    adapter.notifyDataSetChanged()
                }
            }
    }
}
