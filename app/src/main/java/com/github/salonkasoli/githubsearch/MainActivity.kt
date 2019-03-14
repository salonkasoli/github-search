package com.github.salonkasoli.githubsearch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.github.salonkasoli.githubsearch.login.SignInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}
