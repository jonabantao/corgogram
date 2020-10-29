package com.abantaoj.corgogram.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abantaoj.corgogram.R
import com.abantaoj.corgogram.models.Post
import com.bumptech.glide.Glide

class FeedAdapter(private val context: Context, private val posts: MutableList<Post>) :
    RecyclerView.Adapter<FeedAdapter.ViewHolder>() {
     class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var usernameTextView: TextView = itemView.findViewById(R.id.itemFeedUser)
        private var postImageView: ImageView = itemView.findViewById(R.id.itemFeedPhoto)
        private var descriptionTextView: TextView = itemView.findViewById(R.id.itemFeedDescription)

        fun bind(post: Post) {
            usernameTextView.text = post.user!!.username
            val parseFile = post.image
            if (parseFile != null) {
                Glide.with(itemView.context)
                    .load(parseFile.url)
                    .into(postImageView)
            }
            descriptionTextView.text = post.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
