package com.github.salonkasoli.githubsearch

import android.content.Context
import android.widget.Toast

class Toaster(
    private val context: Context
) {
    fun show(string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }
}