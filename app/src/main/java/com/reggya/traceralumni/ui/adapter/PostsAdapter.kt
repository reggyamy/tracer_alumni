package com.reggya.traceralumni.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reggya.traceralumni.BuildConfig.PHOTO_URL
import com.reggya.traceralumni.R
import com.reggya.traceralumni.UserActivity
import com.reggya.traceralumni.UserActivity.Companion.EXTRA_ID
import com.reggya.traceralumni.core.data.model.PostResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.PostItemBinding

class PostsAdapter: RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private var posts = ArrayList<PostResponse?>()
    var btCommentOnClick: ((PostResponse?) -> Unit)?  = null
    var bodyComment: ((String) -> Unit)? = null
    var btDislikeOnClick: ((PostResponse?) -> Unit)? = null
    var btLikeOnClick: ((PostResponse?) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(newData: List<PostResponse?>?){
        if (newData != null){
            posts.clear()
            posts.addAll(newData)
            notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        if (post != null){
            holder.bind(post)

        }
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(private val binding: PostItemBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(post: PostResponse) {
            binding.name.text = post.author?.name
            binding.alumni.text = post.author?.alumni
            binding.description.text = post.description
            binding.countLikes.text = post.likes?.size.toString() + " " + itemView.context.getString(R.string.suka)
            binding.countComments.text = post.comments?.size.toString() + " " + itemView.context.getString(R.string.komentar)
            Glide.with(itemView.context).load(PHOTO_URL + post.author?.photo).error(R.drawable.ic_avatar).into(binding.photoUser)
            Glide.with(itemView.context).load(PHOTO_URL + post.image).into(binding.image)
            val commentsAdapter = CommentsAdapter(post.comments)
            binding.rvComments.layoutManager = LinearLayoutManager(itemView.context)
            binding.rvComments.adapter = commentsAdapter

            val intent = Intent(itemView.context, UserActivity::class.java)
            intent.putExtra(EXTRA_ID, post.author?.userId)

            binding.photoUser.setOnClickListener { itemView.context.startActivity(intent)}
            binding.name.setOnClickListener { itemView.context.startActivity(intent)}

            val preference = LoginPreference(itemView.context)
            val name = preference.getName().toString()
            val findName = post.likes?.find {
                it?.name == name
            }

            if (findName != null){
                binding.btLike.setImageResource(R.drawable.ic_heart)
                binding.btLike.setOnClickListener {
                    binding.btLike.setImageResource(R.drawable.ic_dislike)
                    btDislikeOnClick?.invoke(post)
                }
            }else{
                binding.btLike.setImageResource(R.drawable.ic_dislike)
                binding.btLike.setOnClickListener {
                    binding.btLike.setImageResource(R.drawable.ic_heart)
                    btLikeOnClick?.invoke(post)
                }
            }

            var state : Boolean? = null
            state = false
            binding.btComment.setOnClickListener {
                if (state == false){
                    state = true
                    binding.viewChildRV.visibility = View.VISIBLE
                } else{
                    state = false
                    binding.viewChildRV.visibility = View.GONE
                }
                binding.btSend.setOnClickListener {
                    val body = binding.addComment.text.toString()
                    binding.addComment.text.clear()
                    post.body = body
                    btCommentOnClick?.invoke(post)
                }
            }

        }

    }
}

