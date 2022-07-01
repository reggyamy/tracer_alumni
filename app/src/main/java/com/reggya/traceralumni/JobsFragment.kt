package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.data.remote.ApiResponseType
import com.reggya.traceralumni.core.domain.model.JobsModel
import com.reggya.traceralumni.core.utils.DataMapper
import com.reggya.traceralumni.databinding.FragmentJobsBinding
import com.reggya.traceralumni.ui.ConnectionLiveData
import com.reggya.traceralumni.ui.adapter.JobsAdapter
import com.reggya.traceralumni.ui.viewmodel.JobsViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory


class JobsFragment : Fragment(){

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

        checkInternetConnection()
        setUpViewModel()
        findNavController().clearBackStack(R.id.navigation_bookmark)

//        binding.swipeRefresh.setOnRefreshListener(this)
        binding.btSearch.setOnClickListener{
            setSearch()
        }
        binding.searchBar.setOnKeyListener { v, keyCode, event ->
            if (event.action== KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER){
                setSearch()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }



    }

    private fun setUpViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[JobsViewModel::class.java]
    }

    private fun setSearch() {
        val query = binding.searchBar.text.toString().trim()

        viewModel.getSeachJobs(query).observe(viewLifecycleOwner) {
            when (it) {
                ApiResponse.success(it.data) ->{
                    binding.rvJobs.visibility = View.GONE
                    binding.searchNoResult.viewSearchNull.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.GONE
                        setLoading(ApiResponseType.SUCCESS)
                        setUpUi(DataMapper.jobResponsesToDomain(it.data))
                    }, 1000)
                }
                ApiResponse.empty() -> {
                    binding.rvJobs.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    binding.searchNoResult.viewSearchNull.visibility =View.GONE
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.progressBar.visibility = View.GONE
                        setLoading(ApiResponseType.EMPTY)
                    },1000)
                }
                ApiResponse.error(it.message)-> setLoading(ApiResponseType.ERROR)
            }
        }

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

    private fun checkInternetConnection() {
        val cld = ConnectionLiveData(this.requireContext())
        cld.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                setUpViewModel()
                setJobs()
            } else {
                setLoading(ApiResponseType.INTERNET_LOST)
                binding.viewNoConnection.buttonConnect.setOnClickListener {
                    cld.observe(viewLifecycleOwner) { isConnected ->
                        if (isConnected) {
                            setJobs()
                            binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
                            Toast.makeText(this.context, this.getString(R.string.connected), Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this.context, this.getString(R.string.not_connected), Toast.LENGTH_SHORT).show()
                        }
                    }
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

//    override fun onRefresh() {
//        binding.swipeRefresh.isRefreshing = false
//    }

    private fun setLoading(apiResponseType: ApiResponseType) {
        when(apiResponseType){
            ApiResponseType.SUCCESS ->{
                binding.shimmer.visibility = View.GONE
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
                binding.rvJobs.visibility = View.VISIBLE
            }
            ApiResponseType.EMPTY -> {
                binding.searchNoResult.viewSearchNull.visibility = View.VISIBLE
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
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
                binding.viewServerError.buttonConnect.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }
    }

}