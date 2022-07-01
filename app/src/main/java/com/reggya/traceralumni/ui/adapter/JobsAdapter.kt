package com.reggya.traceralumni.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reggya.traceralumni.DetailJobActivity
import com.reggya.traceralumni.DetailJobActivity.Companion.KEY_JOB
import com.reggya.traceralumni.R
import com.reggya.traceralumni.core.domain.model.JobsModel
import com.reggya.traceralumni.databinding.JobItemBinding

class JobsAdapter: RecyclerView.Adapter<JobsAdapter.ViewHolder>() {

    private var jobs = ArrayList<JobsModel>()


    fun setNewDAta(newData: List<JobsModel>?){
        if (newData == null) return
        jobs.clear()
        jobs.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(JobItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val job = jobs[position]
        if (job != null){

            holder.bind(job)
        }
    }

    override fun getItemCount(): Int = jobs.size


    inner class ViewHolder(private val binding: JobItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(job: JobsModel) {
            binding.jobPosition.text = job.jobPosition
            binding.company.text = job.company
            binding.location.text = job.location

            Glide.with(itemView).load(job.image).error(R.drawable.ic_round_business_24).into(binding.image)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailJobActivity::class.java)
                intent.putExtra(KEY_JOB, job)
                itemView.context.startActivity(intent)
            }
        }

    }

}