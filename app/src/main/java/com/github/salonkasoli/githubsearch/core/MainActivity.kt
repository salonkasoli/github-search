package com.github.salonkasoli.githubsearch.core

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.github.salonkasoli.githubsearch.App
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.search.SearchActivity
import com.github.salonkasoli.githubsearch.signin.SignInActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (App.instance.githubUserCache.getGithubUser() != null) {
            startSearchActivity()
            finish()
        }

        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            val intents = Array(2) { position ->
                when(position) {
                    0 -> return@Array Intent(this, SearchActivity::class.java)
                    1 -> return@Array Intent(this, SignInActivity::class.java)
                    else -> null
                }
            }
            ContextCompat.startActivities(this, intents)
            finish()
        }
        findViewById<Button>(R.id.search_button).setOnClickListener {
            startSearchActivity()
        }
    }

    private fun startSearchActivity() {
        startActivity(Intent(this, SearchActivity::class.java))
    }
}
