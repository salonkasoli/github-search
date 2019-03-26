package com.github.salonkasoli.githubsearch.core.cache

import android.util.LruCache
import com.github.salonkasoli.githubsearch.search.model.GithubRepo

class SearchCache {

    private val cache: LruCache<String, MutableList<GithubRepo>> = LruCache(10)

    fun addRepos(query: String, repos: List<GithubRepo>) {
        var cachedRepos = cache.get(query)
        if (cachedRepos == null) {
            cachedRepos = ArrayList()
            cache.put(query, cachedRepos)
        }
        cachedRepos.addAll(repos)
    }

    fun setRepos(query: String, repos: List<GithubRepo>) {
        cache.get(query)?.clear()
        addRepos(query, repos)
    }

    fun getRepos(query: String) : List<GithubRepo>? {
        return cache.get(query)
    }
}