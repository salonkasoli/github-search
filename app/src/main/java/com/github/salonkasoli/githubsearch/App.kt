package com.github.salonkasoli.githubsearch

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.github.salonkasoli.githubsearch.core.cache.GithubUserCache
import com.github.salonkasoli.githubsearch.core.cache.SearchCache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    // We dont really need DI framework. It should be enough just to make App.instance.{dep}
    lateinit var retrofit: Retrofit
    lateinit var githubUserCache: GithubUserCache
    lateinit var searchCache: SearchCache

    override fun onCreate() {
        instance = this
        super.onCreate()
        Fresco.initialize(this)
        githubUserCache = GithubUserCache(getSharedPreferences("user_cache", Context.MODE_PRIVATE))
        searchCache = SearchCache()
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
//                    .addInterceptor(HttpLoggingInterceptor { message ->
//                        Log.wtf("lol", message)
//                    }.apply {
//                        level = HttpLoggingInterceptor.Level.BODY
//                    })
                    .build()
            )
            .build()

    }
}