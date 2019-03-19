package com.github.salonkasoli.githubsearch.search.rv

import android.content.Context
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.github.salonkasoli.githubsearch.R
import com.github.salonkasoli.githubsearch.search.model.GithubRepo

class ReposListAdapter : RecyclerView.Adapter<ReposListAdapter.RepoViewHolder>() {

    private var repos: List<GithubRepo> = ArrayList()
    fun setRepost(repos: List<GithubRepo>) {
        this.repos = repos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return RepoViewHolder(view as ViewGroup)
    }

    override fun getItemCount(): Int {
        return repos.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repo = repos[position]
        holder.title.text = repo.name

        val dp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            46f,
            holder.context.resources.displayMetrics
        ).toInt()

        val imageRequest = ImageRequestBuilder.newBuilderWithSource(Uri.parse(repo.owner.avatarUrl))
            .setResizeOptions(ResizeOptions(dp, dp))
            .build()
        holder.avatar.controller = Fresco.newDraweeControllerBuilder()
            .setOldController(holder.avatar.controller)
            .setImageRequest(imageRequest)
            .build()
    }


    class RepoViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val avatar: SimpleDraweeView = view.findViewById(R.id.avatar)
        val context: Context = view.context
    }
}