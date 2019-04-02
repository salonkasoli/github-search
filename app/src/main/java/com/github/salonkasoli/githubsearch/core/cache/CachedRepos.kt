package com.github.salonkasoli.githubsearch.core.cache

import com.github.salonkasoli.githubsearch.search.model.GithubRepo

data class CachedRepos(
    val repos: List<GithubRepo>,
    val loadedPages: Int
)