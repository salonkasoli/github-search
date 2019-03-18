package com.github.salonkasoli.githubsearch.search.model

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    @SerializedName("name")
    val name: String
)