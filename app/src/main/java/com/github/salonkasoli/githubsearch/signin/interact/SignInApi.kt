package com.github.salonkasoli.githubsearch.signin.interact

import com.github.salonkasoli.githubsearch.signin.model.GithubUser
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SignInApi {
    @GET("user")
    fun signIn(@Header("Authorization") authToken: String) : Call<GithubUser>
}