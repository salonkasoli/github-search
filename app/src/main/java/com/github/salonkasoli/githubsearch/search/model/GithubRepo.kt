package com.github.salonkasoli.githubsearch.search.model

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    @SerializedName("full_name")
    val name: String,
    @SerializedName("owner")
    val owner: GithubRepoOwner
)