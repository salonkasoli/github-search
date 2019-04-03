package com.github.salonkasoli.githubsearch.signin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.salonkasoli.githubsearch.App
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.common.Toaster
import com.github.salonkasoli.githubsearch.core.cache.GithubUserCache
import com.github.salonkasoli.githubsearch.search.SearchActivity
import com.github.salonkasoli.githubsearch.signin.interact.AuthInteractor
import com.github.salonkasoli.githubsearch.signin.ui.SignInWidget
import com.github.salonkasoli.githubsearch.signin.ui.SignOutWidget

class SignInActivity : AppCompatActivity() {

    private lateinit var authInteractor: AuthInteractor
    private lateinit var toaster: Toaster

    private lateinit var loadingView: View
    private lateinit var signInWidget: SignInWidget
    private lateinit var signOutWidget: SignOutWidget

    private lateinit var githubUserCache: GithubUserCache

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        this.githubUserCache = App.instance.githubUserCache
        loadingView = findViewById(R.id.loading_view)
        this.authInteractor = AuthInteractor(
            App.instance.retrofit,
            App.instance.githubUserCache
        )
        this.toaster = Toaster(this)
        this.signInWidget = SignInWidget(
            findViewById(R.id.sign_in_view),
            findViewById(R.id.login_edit_text),
            findViewById(R.id.password_edit_text),
            findViewById(R.id.sign_in_button)
        )
        this.signOutWidget = SignOutWidget(
            findViewById(R.id.sign_out_view),
            findViewById(R.id.username),
            findViewById(R.id.button_sign_out),
            findViewById(R.id.button_search)
        )

        signInWidget.setOnSignInClicked { username, password ->
            // here we should auth user
            authInteractor.auth(username, password)
        }
        signOutWidget.setOnSignOutClickedListener {
            githubUserCache.setGithubUser(null)
            showCorrectView()
        }
        signOutWidget.setGoToSearchListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        authInteractor.setFailCallback {
            showCorrectView()
            toaster.show("smth wrong")
        }
        authInteractor.setSuccessCallback {
            showCorrectView()
        }
        authInteractor.setLoadingCallback {
            loadingView.visibility = View.VISIBLE
        }

        showCorrectView()
    }

    override fun onPause() {
        super.onPause()
        authInteractor.setSuccessCallback(null)
        authInteractor.setSuccessCallback(null)
        authInteractor.setLoadingCallback(null)
    }

    private fun showCorrectView() {
        loadingView.visibility = View.GONE
        val user = githubUserCache.getGithubUser()
        if (user != null) {
            signOutWidget.show(user.username)
            signInWidget.hide()
        } else {
            signOutWidget.hide()
            signInWidget.show()
        }
    }
}