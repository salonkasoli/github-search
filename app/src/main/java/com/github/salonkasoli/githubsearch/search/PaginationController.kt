package com.github.salonkasoli.githubsearch.search

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PaginationController(
    private val list: RecyclerView,
    private val adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>,
    private val loadingProvider: (Unit) -> Boolean,
    private val loadMoreListener: (Unit) -> Unit,
    lifecycle: Lifecycle
) : LifecycleObserver {

    private val layoutManager = list.layoutManager as LinearLayoutManager

    var isEnabled = true

    init {
        lifecycle.addObserver(this)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        list.addOnScrollListener(listener)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        list.removeOnScrollListener(listener)
    }

    private val listener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (!isEnabled) {
                return
            }
            if (
                !loadingProvider.invoke(Unit) &&
                (adapter.itemCount - layoutManager.findLastVisibleItemPosition() < 5)
            ) {
                loadMoreListener.invoke(Unit)
            }
        }
    }
}