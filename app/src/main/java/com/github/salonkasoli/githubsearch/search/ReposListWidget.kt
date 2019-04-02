package com.github.salonkasoli.githubsearch.search

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.salonkasoli.githubsearch.search.model.GithubRepo
import com.github.salonkasoli.githubsearch.search.rv.ReposListAdapter

class ReposListWidget(
    private val adapter: ReposListAdapter,
    private val loadingView: View,
    private val swipeRefreshLayout: SwipeRefreshLayout
) {

    fun showRepos(repos: List<GithubRepo>?) {
        loadingView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
        adapter.repos = repos ?: ArrayList()
        adapter.notifyDataSetChanged()
        adapter.isLoadingMore = false
    }

    fun showLoading() {
        adapter.isLoadingMore = false
        if (adapter.repos.isEmpty()) {
            loadingView.visibility = View.VISIBLE
            swipeRefreshLayout.isRefreshing = false
        } else {
            loadingView.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = true
        }
    }

    fun showLoadMore() {
        loadingView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = true
        adapter.isLoadingMore = true
    }

    fun isLoading(): Boolean {
        return loadingView.visibility == View.VISIBLE ||
                swipeRefreshLayout.isRefreshing ||
                adapter.isLoadingMore
    }
}