package com.github.salonkasoli.githubsearch.signin.ui

import android.view.View
import android.widget.TextView

class SignOutWidget(
    private val container: View,
    private val usernameTextView: TextView,
    private val signOutButton: View,
    private val searchButton: View
) {

    fun show(username: String) {
        usernameTextView.setText("Signed in as $username")
        container.visibility = View.VISIBLE
    }

    fun hide() {
        container.visibility = View.GONE
    }

    fun setOnSignOutClickedListener(listener: (Unit) -> Unit) {
        signOutButton.setOnClickListener {
            listener.invoke(Unit)
        }
    }

    fun setGoToSearchListener(listener: (Unit) -> Unit) {
        searchButton.setOnClickListener {
            listener.invoke(Unit)
        }
    }
}