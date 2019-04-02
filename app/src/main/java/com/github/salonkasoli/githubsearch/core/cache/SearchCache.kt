package com.github.salonkasoli.githubsearch.core.cache

import android.util.Log
import android.util.LruCache
import com.github.salonkasoli.githubsearch.search.model.GithubRepo

class SearchCache {

    private val cache: LruCache<String, CachedRepos> = LruCache(10)

    fun addRepos(query: String, repos: List<GithubRepo>) {
        var cachedRepos = cache.get(query)
        if (cachedRepos == null) {
            cachedRepos = CachedRepos(ArrayList(), 0)
        }
        val newList = ArrayList(cachedRepos.repos)
        newList.addAll(repos)
        val newCache = CachedRepos(newList, cachedRepos.loadedPages + 1)
        cache.put(query, newCache)
    }

    fun setRepos(query: String, repos: List<GithubRepo>) {
        cache.put(query, CachedRepos(ArrayList(), 0))
        addRepos(query, repos)
    }

    fun getRepos(query: String) : CachedRepos? {
        return cache.get(query)
    }
}