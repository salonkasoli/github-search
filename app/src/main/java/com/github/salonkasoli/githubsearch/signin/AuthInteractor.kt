package com.github.salonkasoli.githubsearch.signin

import okhttp3.Credentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AuthInteractor(
    private val retrofit: Retrofit
) {

    private var failCallback: ((Unit) -> (Unit))? = null
    private var successCallback: ((authToken: String) -> (Unit))? = null
    private var loadingCallback: ((Unit) -> (Unit))? = null

    /**
     * Force repository to auth.
     * Result you can get through callbacks passed via
     */
    fun auth(login: String, password: String) {
        val token = Credentials.basic(login, password)
        loadingCallback?.invoke(Unit)
        retrofit.create(SignInApi::class.java).signIn(token).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() == 200) {
                    successCallback?.invoke(token)
                } else {
                    failCallback?.invoke(Unit)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
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