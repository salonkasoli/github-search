package com.github.salonkasoli.githubsearch.signin.interact

import com.github.salonkasoli.githubsearch.core.cache.GithubUserCache
import com.github.salonkasoli.githubsearch.signin.model.GithubUser
import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AuthInteractor(
    private val retrofit: Retrofit,
    private val githubUserCache: GithubUserCache
) {

    private var failCallback: ((Unit) -> (Unit))? = null
    private var successCallback: ((authToken: String) -> (Unit))? = null
    private var loadingCallback: ((Unit) -> (Unit))? = null

    /**
     * Force repository to auth.
     * Result you can get through callbacks which was passed to interactor.
     */
    fun auth(login: String, password: String) {
        val token = Credentials.basic(login, password)
        loadingCallback?.invoke(Unit)
        retrofit.create(SignInApi::class.java).signIn(token).enqueue(object : Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                if (response.code() == 200) {
                    githubUserCache.setAuthToken(token)
                    githubUserCache.setGithubUser(response.body() as GithubUser)
                    successCallback?.invoke(token)
                } else {
                    githubUserCache.setAuthToken(null)
                    githubUserCache.setGithubUser(null)
                    failCallback?.invoke(Unit)
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                failCallback?.invoke(Unit)
            }
        })
    }

    fun setFailCallback(callback: ((Unit) -> (Unit))?) {
        this.failCallback = callback
    }

    fun setSuccessCallback(callback: ((authToken: String) -> (Unit))?) {
        this.successCallback = callback
    }

    fun setLoadingCallback(callback: ((Unit) -> (Unit))?) {
        this.loadingCallback = callback
    }
}