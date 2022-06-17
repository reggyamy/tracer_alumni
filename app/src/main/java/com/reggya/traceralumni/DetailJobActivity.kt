package com.reggya.traceralumni

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.reggya.traceralumni.databinding.ActivityDetailJobBinding
import com.reggya.traceralumni.databinding.ContentDetailJobBinding
import com.reggya.traceralumni.core.domain.model.JobsModel
import com.reggya.traceralumni.ui.viewmodel.JobsViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory

class DetailJobActivity : AppCompatActivity() {

    companion object{
        const val KEY_JOB = "detail job"
    }

    private lateinit var binding: ActivityDetailJobBinding
    private lateinit var binding_ : ContentDetailJobBinding
    private lateinit var viewModel: JobsViewModel
    private var state : Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailJobBinding.inflate(layoutInflater)
        binding_ = binding.contentJobDetail
        setContentView(binding.root)

        val job = intent.getParcelableExtra<JobsModel>(KEY_JOB)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[JobsViewModel::class.java]

        checkBookmark(job)
        setUI(job)

    }

    private fun setUI(job: JobsModel?) {
        if(job != null){
            state = job.isBookmark
//            setButtonBookmark(state)

            val descHtml = job.description?.replace("&lt;", "<")?.replace("&gt;",">")

            binding_.tvJobPosition.text = job.jobPosition
            binding_.tvCompany.text = job.company
            binding_.tvLocation.text = job.location
            binding_.tvJobLevel.text = job.jobLevel
            binding_.tvDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(descHtml.toString(), Html.FROM_HTML_MODE_LEGACY)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(descHtml.toString())
            }

            Glide.with(this@DetailJobActivity).load(job.image).error(R.drawable.ic_round_business_24).into( binding_.image)
        }


        binding_.btApply.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(job?.linkJob)))
        }

        binding_.btBookmark.setOnClickListener {
            bookmark(job)
        }
    }

    private fun bookmark(job: JobsModel?) {
        if (state == true){
            state = false
            job?.let { viewModel.deleteJobBookmark(it) }
            stateButton(false)
        }else{
            state = true
            job?.let { viewModel.insertJobBookmark(it) }
            stateButton(true)
        }
    }

    private fun stateButton(state: Boolean) {
        if (state) binding_.btBookmark.setImageResource(R.drawable.ic_bookmark_filled)
        else binding_.btBookmark.setImageResource(R.drawable.ic_bookmark_unfilled)
    }

    private fun checkBookmark(job: JobsModel?) {
        viewModel.checkBookmarked(job?.id).observe(this) {
            if(job?.id != null){
                if(job.id.toInt() > 0){
                    state = true
                    stateButton(true)
                }else{
                    state = false
                    stateButton(false)
                }

            }
        }

    }

//    private fun setButtonBookmark(state: Boolean) {
//        if(state) binding_.btBookmark.setImageResource(R.drawable.ic_bookmark_filled)
//        else binding_.btBookmark.setImageResource(R.drawable.ic_bookmark_unfilled)
//    }



}