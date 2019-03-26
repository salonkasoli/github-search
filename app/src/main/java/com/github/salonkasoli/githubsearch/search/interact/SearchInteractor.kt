package com.github.salonkasoli.githubsearch.search.interact

import com.github.salonkasoli.githubsearch.search.model.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class SearchInteractor(
    private val retrofit: Retrofit
) {

    private var failCallback: ((Unit) -> (Unit))? = null
    private var successCallback: ((searchResponse: SearchResponse, repoName: String) -> (Unit))? = null
    private var loadingCallback: ((Unit) -> (Unit))? = null


    /**
     * Searches repos by name.
     */
    fun search(repoName: String) {
        loadingCallback?.invoke(Unit)
        val query = "$repoName in:name"
        retrofit.create(SearchApi::class.java).search(query, 1).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.code() == 200) {
                    successCallback?.invoke(response.body()!!, repoName)
                } else {
                    failCallback?.invoke(Unit)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                failCallback?.invoke(Unit)
            }
        })
    }

    fun setFailCallback(callback: ((Unit) -> (Unit))?) {
        this.failCallback = callback
    }

    fun setSuccessCallback(callback: ((searchResponse: SearchResponse, repoName: String) -> (Unit))?) {
        this.successCallback = callback
    }

    fun setLoadingCallback(callback: ((Unit) -> (Unit))?) {
        this.loadingCallback = callback
    }
}