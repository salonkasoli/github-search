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

    private lateinit var interactor: SearchInteractor
    private lateinit var toaster: Toaster
    private lateinit var cache: SearchCache
    // I HATE YOU, SEARCH VIEW!!!
    private lateinit var searchView: SearchView
    private lateinit var paginationController: PaginationController
    private lateinit var reposListWidget: ReposListWidget

    // Query to search.
    private lateinit var searchQuery: String

    // Query that user input to searchView. null if searchView is collapsed
    private var userInput: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val list = findViewById<RecyclerView>(R.id.list)
        val layoutManager = LinearLayoutManager(this)
        list.layoutManager = layoutManager
        val adapter = ReposListAdapter()
        list.adapter = adapter
        this.paginationController = PaginationController(
            list,
            adapter,
            { return@PaginationController this.reposListWidget.isLoading() },
            { searchMore(searchQuery) },
            lifecycle
        )

        this.cache = App.instance.searchCache
        this.interactor = SearchInteractor(App.instance.retrofit)
        this.toaster = Toaster(this)
        this.searchQuery = savedInstanceState?.getString(BUNDLE_QUERY) ?: DEFAULT_SEARCH_QUERY
        this.userInput = savedInstanceState?.getString(BUNDLE_USER_INPUT)
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_layout)
        this.reposListWidget = ReposListWidget(
            adapter,
            findViewById(R.id.loading_view),
            swipeRefreshLayout
        )
        swipeRefreshLayout.setOnRefreshListener {
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
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
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
            if (repoName != searchQuery) {
                return@setLoadingCallback
            }
            reposListWidget.showLoading()
        }
        interactor.setSuccessCallback { searchResponse, repoName ->
            cache.setRepos(repoName, searchResponse.items)
            if (repoName != searchQuery) {
                return@setSuccessCallback
            }
            reposListWidget.showRepos(cache.getRepos(repoName)?.repos)
        }
        interactor.setLoadMoreSuccessCallback { searchResponse, repoName ->
            cache.addRepos(repoName, searchResponse.items)
            if (repoName != searchQuery) {
                return@setLoadMoreSuccessCallback
            }
            if (searchResponse.items.isEmpty()) {
                paginationController.isEnabled = false
            }
            reposListWidget.showRepos(cache.getRepos(searchQuery)?.repos)
        }
        interactor.setLoadMoreLoadingCallback { repoName ->
            if (repoName != searchQuery) {
                return@setLoadMoreLoadingCallback
            }
            reposListWidget.showLoadMore()
        }
        val cachedRepos = cache.getRepos(searchQuery)?.repos
        reposListWidget.showRepos(cachedRepos)
        if (cachedRepos == null || cachedRepos.isEmpty()) {
            search(searchQuery)
        }
    }

    override fun onPause() {
        super.onPause()
        interactor.setSuccessCallback(null)
        interactor.setLoadingCallback(null)
        interactor.setLoadMoreSuccessCallback(null)
        interactor.setLoadMoreLoadingCallback(null)
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
        paginationController.isEnabled = true
        if (TextUtils.isEmpty(query)) {
            searchQuery = DEFAULT_SEARCH_QUERY
        }
        interactor.search(searchQuery)
    }

    private fun searchMore(query: String?) {
        paginationController.isEnabled = true
        if (TextUtils.isEmpty(query)) {
            searchQuery = DEFAULT_SEARCH_QUERY
        }
        var newPage = cache.getRepos(searchQuery)?.loadedPages
        if (newPage == null) {
            newPage = 0
        } else {
            newPage += 1
        }
        interactor.searchMore(searchQuery, newPage)
    }
}