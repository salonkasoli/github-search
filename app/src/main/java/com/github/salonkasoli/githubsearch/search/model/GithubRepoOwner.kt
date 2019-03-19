package com.github.salonkasoli.githubsearch.search.model

import com.google.gson.annotations.SerializedName

data class GithubRepoOwner(
    @SerializedName("login")
    val login: String,
    @SerializedName("avatar_url")
    val avatarUrl: String
)