package com.github.salonkasoli.githubsearch.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.salonkasoli.githubsearch.App
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.common.Toaster
import com.github.salonkasoli.githubsearch.search.interact.SearchInteractor
import com.github.salonkasoli.githubsearch.search.model.GithubRepo
import com.github.salonkasoli.githubsearch.search.rv.ReposListAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var interactor: SearchInteractor
    private lateinit var toaster: Toaster

    private lateinit var adapter: ReposListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        this.adapter = ReposListAdapter()
        list.adapter = adapter

        this.interactor = SearchInteractor(App.instance.retrofit)
        this.toaster = Toaster(this)
    }

    override fun onResume() {
        super.onResume()
        interactor.setLoadingCallback {
            toaster.show("loading")
        }
        interactor.setSuccessCallback {searchResponse ->
            toaster.show("loaded")
            adapter.setRepost(searchResponse.items)
            adapter.notifyDataSetChanged()
        }
        interactor.search("android")
    }

    override fun onPause() {
        super.onPause()
        interactor.setSuccessCallback(null)
        interactor.setLoadingCallback(null)
    }
}