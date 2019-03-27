package com.github.salonkasoli.githubsearch.search

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.salonkasoli.githubsearch.App
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.common.Toaster
import com.github.salonkasoli.githubsearch.core.cache.SearchCache
import com.github.salonkasoli.githubsearch.search.interact.SearchInteractor
import com.github.salonkasoli.githubsearch.search.rv.ReposListAdapter

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val BUNDLE_QUERY = "search_query"
        private const val BUNDLE_USER_INPUT = "user_input"

        // Github doesn't allow to search on empty query.
        // Lets search for 'android'
        private const val DEFAULT_SEARCH_QUERY = "android"
    }

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var loadingView: View
    private lateinit var interactor: SearchInteractor
    private lateinit var toaster: Toaster
    private lateinit var adapter: ReposListAdapter
    private lateinit var cache: SearchCache
    // I HATE YOU, SEARCH VIEW!!!
    private lateinit var searchView: SearchView

    // Query to search.
    private lateinit var searchQuery: String

    // Query that user input to searchView. null if searchView is collapsed
    private var userInput: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        this.loadingView = findViewById(R.id.loading_view)
        this.swipeLayout = findViewById(R.id.swipe_layout)
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        this.adapter = ReposListAdapter()
        list.adapter = adapter

        this.cache = App.instance.searchCache
        this.interactor = SearchInteractor(App.instance.retrofit)
        this.toaster = Toaster(this)
        this.searchQuery = savedInstanceState?.getString(BUNDLE_QUERY) ?: DEFAULT_SEARCH_QUERY
        this.userInput = savedInstanceState?.getString(BUNDLE_USER_INPUT)

        swipeLayout.setOnRefreshListener {
            interactor.search(searchQuery)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.search_button) as MenuItem
        // this crazy shit makes searchview's width match parent
        // Android, i hate you!
        this.searchView = item.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        item.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                // here we have to set userInput to null
                // moreover, if we just change orientation and pressed "back"
                // then 'lupa' icon will not be shown.
                // invalidateOptionMenu solves problem.
                userInput = null
                invalidateOptionsMenu()
                return true
            }

        })
        // important to do it before onQueryTextListener assign,
        // because after expandActionView onQueryTextChange is called with empty string.
        if (userInput != null) {
            item.expandActionView()
            searchView.setQuery(userInput, false)
            searchView.clearFocus()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchQuery = query
                    search(searchQuery)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                userInput = newText
                return false
            }
        })
        return true
    }

    override fun onResume() {
        super.onResume()
        interactor.setLoadingCallback { repoName ->
            if (repoName == searchQuery) {
                if (adapter.repos.isEmpty()) {
                    loadingView.visibility = View.VISIBLE
                    swipeLayout.isRefreshing = false
                } else {
                    loadingView.visibility = View.GONE
                    swipeLayout.isRefreshing = true
                }
            }
        }
        interactor.setSuccessCallback { searchResponse, repoName ->
            cache.setRepos(repoName, searchResponse.items)
            if (repoName == searchQuery) {
                loadingView.visibility = View.GONE
                swipeLayout.isRefreshing = false
                adapter.repos = searchResponse.items
                adapter.notifyDataSetChanged()
            }
        }
        adapter.repos = cache.getRepos(searchQuery) ?: ArrayList()
        adapter.notifyDataSetChanged()
        search(searchQuery)
    }

    override fun onPause() {
        super.onPause()
        interactor.setSuccessCallback(null)
        interactor.setLoadingCallback(null)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(BUNDLE_QUERY, searchQuery)
        outState?.putString(BUNDLE_USER_INPUT, userInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState?.getString(BUNDLE_QUERY) ?: DEFAULT_SEARCH_QUERY
        userInput = savedInstanceState?.getString(BUNDLE_USER_INPUT)
    }

    private fun search(query: String?) {
        if (TextUtils.isEmpty(query)) {
            searchQuery = DEFAULT_SEARCH_QUERY
        }
        interactor.search(searchQuery)
    }
}