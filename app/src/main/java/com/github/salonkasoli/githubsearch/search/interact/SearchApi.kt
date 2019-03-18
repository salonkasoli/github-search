package com.github.salonkasoli.githubsearch.search.interact

import com.github.salonkasoli.githubsearch.search.model.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("search/repositories")
    fun search(@Query("q") query: String, @Query("page") page: Int) : Call<SearchResponse>
}