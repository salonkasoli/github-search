package com.github.salonkasoli.githubsearch.search.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.search.model.GithubRepo

class ReposListAdapter : RecyclerView.Adapter<ReposListAdapter.RepoViewHolder>() {

    private var repos: List<GithubRepo> = ArrayList()

    fun setRepost(repos: List<GithubRepo>) {
        this.repos = repos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return RepoViewHolder(view as TextView)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.view.text = repos[position].name
    }


    class RepoViewHolder(val view: TextView) : RecyclerView.ViewHolder(view)
}