package com.github.salonkasoli.githubsearch.signin.ui

import android.view.View
import android.widget.EditText

class SignInWidget(
    private val container: View,
    private val username: EditText,
    private val password: EditText,
    private val signInButton: View
) {

    fun show() {
        container.visibility = View.VISIBLE
    }

    fun hide() {
        container.visibility = View.GONE
    }

    fun setOnSignInClicked(listener: (username: String, password: String) -> (Unit)) {
        signInButton.setOnClickListener {
            listener.invoke(username.text.toString(), password.text.toString())
        }
    }

}