package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.data.remote.ApiResponseType
import com.reggya.traceralumni.databinding.FragmentJobsBinding
import com.reggya.traceralumni.core.domain.model.JobsModel
import com.reggya.traceralumni.ui.adapter.JobsAdapter
import com.reggya.traceralumni.ui.viewmodel.JobsViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import com.reggya.traceralumni.core.utils.DataMapper
import com.reggya.traceralumni.ui.ConnectionLiveData


class JobsFragment : Fragment() {

    private lateinit var binding: FragmentJobsBinding
    private lateinit var viewModel: JobsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.hide()
        setUpViewModel()
        setJobs()
        setSearch()
        if (!checkInternetConnection()){
            setLoading(ApiResponseType.INTERNET_LOST)
            binding.viewNoConnection.buttonConnect.setOnClickListener {
                checkInternetConnection()
                if (checkInternetConnection()) {
                    setLoading(ApiResponseType.SUCCESS)
                    Toast.makeText(this.context, this.getString(R.string.connected), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this.context, this.getString(R.string.not_connected), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setUpViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[JobsViewModel::class.java]
    }

    private fun setSearch() {

        binding.btSearch.setOnClickListener{
             val query = binding.searchBar.text.toString().trim()

            viewModel.getSeachJobs(query).observe(viewLifecycleOwner) {
                val result = it.data
                when (it) {
                    ApiResponse.success(it.data) ->{
                        setLoading(ApiResponseType.SUCCESS)
                        setUpUi(DataMapper.jobResponsesToDomain(it.data))
                    }
                    ApiResponse.empty() -> setLoading(ApiResponseType.EMPTY)
                    ApiResponse.error(it.message)-> setLoading(ApiResponseType.ERROR)
                }
            }
        }

    }

    private fun setLoading(apiResponseType: ApiResponseType) {
        when(apiResponseType){
            ApiResponseType.SUCCESS ->{
                binding.shimmer.visibility = View.GONE
                binding.viewNoConnection.layoutNoConnection.visibility = View.INVISIBLE
                binding.viewServerError.layoutServerDown.visibility = View.INVISIBLE
                binding.rvJobs.visibility = View.VISIBLE
            }
            ApiResponseType.EMPTY -> {
                binding.searchNoResult.viewSearchNull.visibility = View.VISIBLE
                binding.viewNoConnection.layoutNoConnection.visibility = View.INVISIBLE
                binding.viewServerError.layoutServerDown.visibility = View.INVISIBLE
                binding.shimmer.visibility = View.GONE
                binding.rvJobs.visibility = View.GONE
            }
            ApiResponseType.INTERNET_LOST -> {
                binding.viewNoConnection.layoutNoConnection.visibility = View.VISIBLE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
                binding.shimmer.visibility = View.GONE
                binding.rvJobs.visibility = View.GONE
            }
            ApiResponseType.ERROR -> {
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
                binding.viewServerError.layoutServerDown.visibility = View.VISIBLE
                binding.shimmer.visibility = View.GONE
                binding.rvJobs.visibility = View.GONE
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = ConnectionLiveData()
        return connectivityManager.checkInternetConnection(this.requireContext())
    }

    private fun setJobs() {
        viewModel.getJobs().observe(viewLifecycleOwner) { response ->
            when (response) {
                ApiResponse.success(response.data) -> {
                    setLoading(ApiResponseType.SUCCESS)
                    setUpUi(DataMapper.jobResponsesToDomain(response.data))
                }
                ApiResponse.empty() -> {
                    setLoading(ApiResponseType.EMPTY)
                }
                ApiResponse.error(response.message) -> {
                    setLoading(ApiResponseType.ERROR)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpUi(jobs: List<JobsModel>?) {
        val jobsAdapter = JobsAdapter()
        binding.rvJobs.layoutManager = LinearLayoutManager(context)
        jobsAdapter.setNewDAta(jobs?.reversed())
        jobsAdapter.notifyDataSetChanged()

        binding.rvJobs.apply {
            setHasFixedSize(true)
            adapter = jobsAdapter
        }
    }


}