package com.github.salonkasoli.githubsearch.core.cache

import android.content.SharedPreferences
import com.github.salonkasoli.githubsearch.signin.model.GithubUser

class GithubUserCache(
    private val prefs: SharedPreferences
) {

    companion object {
        private const val PREF_USERNAME = "username"
        private const val PREF_AVATAR = "avatar"
        private const val PREF_TOKEN = "auth_token"
    }

    fun setGithubUser(user: GithubUser?) {
        prefs.edit()
            .putString(PREF_USERNAME, user?.username)
            .putString(PREF_AVATAR, user?.avatarUrl)
            .apply()
    }

    fun getGithubUser() : GithubUser? {
        val name = prefs.getString(PREF_USERNAME, null)
        val avatar = prefs.getString(PREF_AVATAR, null)
        return if (name != null && avatar != null) {
            GithubUser(name, avatar)
        } else {
            null
        }
    }

    fun setAuthToken(token: String?) {
        prefs.edit()
            .putString(PREF_TOKEN, token)
            .apply()
    }

    fun getAuthToken() : String? {
        return prefs.getString(PREF_TOKEN, null)
    }

}