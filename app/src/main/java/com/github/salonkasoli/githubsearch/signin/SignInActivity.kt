package com.github.salonkasoli.githubsearch.signin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.salonkasoli.githubsearch.App
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.common.Toaster
import com.github.salonkasoli.githubsearch.signin.interact.AuthInteractor

class SignInActivity : AppCompatActivity() {

    private lateinit var authInteractor: AuthInteractor
    private lateinit var toaster: Toaster
    private lateinit var loadingView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        loadingView = findViewById(R.id.loading_view)
        this.authInteractor = AuthInteractor(
            App.instance.retrofit,
            App.instance.githubUserCache
        )
        this.toaster = Toaster(this)
        val loginEditText = findViewById<EditText>(R.id.login_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.password_edit_text)
        findViewById<Button>(R.id.sign_in_button).setOnClickListener {
            // here we should auth user
            authInteractor.auth(loginEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        authInteractor.setFailCallback {
            loadingView.visibility = View.GONE
            toaster.show("smth wrong")
        }
        authInteractor.setSuccessCallback {
            loadingView.visibility = View.GONE
            toaster.show("success")
        }
        authInteractor.setLoadingCallback {
            loadingView.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        authInteractor.setSuccessCallback(null)
        authInteractor.setSuccessCallback(null)
        authInteractor.setLoadingCallback(null)
    }
}