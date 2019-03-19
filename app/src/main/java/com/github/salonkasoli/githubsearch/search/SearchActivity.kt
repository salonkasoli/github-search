package com.github.salonkasoli.githubsearch.search

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchView = menu?.findItem(R.id.search_button)?.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    interactor.search(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
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