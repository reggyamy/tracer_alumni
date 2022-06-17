package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.reggya.traceralumni.databinding.FragmentBookmarkBinding
import com.reggya.traceralumni.ui.viewmodel.JobsViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import com.reggya.traceralumni.ui.adapter.JobsAdapter

class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: JobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[JobsViewModel::class.java]

        val jobAdapter = JobsAdapter()
        viewModel.getJobBookmarks().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.noBookmarks.visibility = View.GONE
                binding.rvJob.visibility = View.VISIBLE
                jobAdapter.setNewDAta(it)
                jobAdapter.notifyDataSetChanged()
            }else{
                binding.noBookmarks.visibility = View.VISIBLE
                binding.rvJob.visibility = View.GONE
                binding.btSearchJob.setOnClickListener {
                   findNavController().navigate(R.id.navigation_jobs)
                }
            }
        }

        binding.rvJob.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = jobAdapter
        }
    }


}