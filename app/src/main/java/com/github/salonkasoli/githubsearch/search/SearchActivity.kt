package com.github.salonkasoli.githubsearch.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.search.model.GithubRepo
import com.github.salonkasoli.githubsearch.search.rv.ReposListAdapter

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val manager = LinearLayoutManager(this)
        val adapter = ReposListAdapter()
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = manager
        list.adapter = adapter

        adapter.setRepost(arrayListOf(GithubRepo("lol"), GithubRepo("kek"), GithubRepo("cheburek")))
        adapter.notifyDataSetChanged()
    }
}