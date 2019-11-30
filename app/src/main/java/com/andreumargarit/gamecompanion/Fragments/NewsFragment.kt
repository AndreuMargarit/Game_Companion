package com.andreumargarit.gamecompanion.Fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.andreumargarit.gamecompanion.Models.NewModel

import com.andreumargarit.gamecompanion.R
import com.andreumargarit.gamecompanion.Utils.NewsAdapter
import com.andreumargarit.gamecompanion.Utils.UserDao
import kotlinx.android.synthetic.main.fragment_news.*

/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    var news = ArrayList<NewModel>();

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onResume() {
        super.onResume()
        initUI();
    }

    private fun initUI()
    {
        getAllNews()
    }

    private fun getAllNews()
    {
        UserDao().getAllNews(successListener = { _news ->

            recyclerView.adapter = NewsAdapter(ArrayList(_news.orEmpty()))
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

        },
            failureListener = {
                Toast.makeText((requireContext()), it.localizedMessage, Toast.LENGTH_LONG).show();
            })

    }

}
