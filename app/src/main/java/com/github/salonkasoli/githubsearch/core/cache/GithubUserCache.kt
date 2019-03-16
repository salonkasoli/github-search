package com.github.salonkasoli.githubsearch.core.cache

import com.github.salonkasoli.githubsearch.signin.model.GithubUser

class GithubUserCache {

    private var user: GithubUser? = null
    private var token: String? = null

    fun setGithubUser(user: GithubUser?) {
        this.user = user
    }

    fun setAuthToken(token: String?) {
        this.token = token
    }

    fun getAuthToken() : String? {
        return this.token
    }
}