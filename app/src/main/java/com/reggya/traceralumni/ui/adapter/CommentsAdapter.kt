package com.reggya.traceralumni.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reggya.traceralumni.BuildConfig
import com.reggya.traceralumni.R
import com.reggya.traceralumni.core.data.model.CommentsItem
import com.reggya.traceralumni.databinding.CommentItemBinding

class CommentsAdapter(comments: List<CommentsItem?>?) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private var comments : List<CommentsItem?> = ArrayList()

    init {
        if (comments != null) {
            this.comments = comments
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(CommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        if (comment != null) {
            holder.bind(comment)
        }
    }

    override fun getItemCount(): Int = comments.size

    inner class ViewHolder(private val binding: CommentItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: CommentsItem) {
            Glide.with(itemView.context)
                .load(BuildConfig.PHOTO_URL + comment.photoProfile)
                .error(R.drawable.ic_avatar)
                .into(binding.image)
            binding.name.text = comment.name
            binding.body.text = comment.body
        }
    }
}