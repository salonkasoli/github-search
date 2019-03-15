package com.github.salonkasoli.githubsearch.signin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.salonkasoli.githubsearch.R

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val loginEditText = findViewById<EditText>(R.id.login_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            // here we should auth user
            Toast.makeText(this, loginEditText.text.toString() + " " + passwordEditText.text.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}