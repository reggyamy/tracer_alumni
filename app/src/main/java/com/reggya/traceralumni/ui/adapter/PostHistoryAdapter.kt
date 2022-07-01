package com.reggya.traceralumni.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reggya.traceralumni.BuildConfig.PHOTO_URL
import com.reggya.traceralumni.R
import com.reggya.traceralumni.core.data.model.PostResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.HistoryItemBinding

class PostHistoryAdapter: RecyclerView.Adapter<PostHistoryAdapter.ViewHolder>() {
    private val posts = ArrayList<PostResponse>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<PostResponse>?) {
        if (newData == null) return posts.clear()
        posts.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int = posts.size


    class ViewHolder(private val binding: HistoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostResponse) {
            Glide.with(itemView.context)
                .load(PHOTO_URL+post.image)
                .override(400,400)
                .into(binding.image)
            binding.description.text = post.description
            binding.countLikes.text = post.likes?.size.toString()
            binding.countComments.text = post.comments?.size.toString()

            val preference = LoginPreference(itemView.context)
            val name = preference.getName().toString()
            val findName = post.likes?.find {
                it?.name == name
            }

            itemView.setOnClickListener{
                Toast.makeText(itemView.context, "Coming Soon", Toast.LENGTH_SHORT).show()
            }

            if (findName != null){
                binding.btLike.setImageResource(R.drawable.ic_heart)
                binding.btLike.setOnClickListener {
                    binding.btLike.setImageResource(R.drawable.ic_dislike)
                }
            }else{
                binding.btLike.setOnClickListener {
                    binding.btLike.setImageResource(R.drawable.ic_heart)
                }
            }

        }
    }

}
