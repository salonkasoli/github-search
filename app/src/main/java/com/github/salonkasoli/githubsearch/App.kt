package com.github.salonkasoli.githubsearch

import android.app.Application
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.github.salonkasoli.githubsearch.core.cache.GithubUserCache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    lateinit var retrofit: Retrofit
    lateinit var githubUserCache: GithubUserCache

    override fun onCreate() {
        instance = this
        super.onCreate()
        Fresco.initialize(this)
        githubUserCache = GithubUserCache()
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        // if we know authToken, just add it to request ad header.
                        val token = githubUserCache.getAuthToken()
                        if (token != null) {
                            val request = chain
                                .request()
                                .newBuilder()
                                .addHeader("Authorization", token)
                                .build()
                            return@addInterceptor chain.proceed(request)
                        } else {
                            return@addInterceptor chain.proceed(chain.request())
                        }
                    }
                    .addInterceptor(HttpLoggingInterceptor { message ->
                        Log.wtf("lol", message)
                    }.apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()

    }
}