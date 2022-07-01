package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.reggya.traceralumni.BuildConfig.PHOTO_URL
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.data.remote.ApiResponseType
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.ContentHomeBinding
import com.reggya.traceralumni.databinding.FragmentHomeBinding
import com.reggya.traceralumni.ui.ConnectionLiveData
import com.reggya.traceralumni.ui.RefreshView
import com.reggya.traceralumni.ui.adapter.PostsAdapter
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ProfileViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import kotlin.system.exitProcess


class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private lateinit var binding : ContentHomeBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var userViewModel: ProfileViewModel
    private val postsAdapter = PostsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = _binding.contentHome
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(this.requireContext())
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
        userViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

//        binding.swipeRefresh.setOnRefreshListener(this)
        val cld = ConnectionLiveData(this.requireContext())
        checkInternetConnection(cld)

    }

    private fun checkInternetConnection(cld: ConnectionLiveData?) {
        cld?.observe(viewLifecycleOwner){ isConnected ->
            if (isConnected){
                setUpViewModel()
                setUpUI()
                Handler(Looper.getMainLooper()).postDelayed({
                    setUpViewModel()
                }, 1000)
            }else {
                setLoading(ApiResponseType.INTERNET_LOST)
                _binding.viewNoConnection.buttonConnect.setOnClickListener {
                    cld.observe(viewLifecycleOwner) { isConnected ->
                        if (isConnected) {
                            setUpViewModel()
                            _binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
                            Toast.makeText(
                                this.context,
                                this.getString(R.string.connected),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this.context,
                                this.getString(R.string.not_connected),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpViewModel() {
        viewModel.getAllPost().observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                ApiResponse.success(apiResponse.data) ->{
                    setLoading(ApiResponseType.SUCCESS)
                    postsAdapter.setNewData(apiResponse.data?.reversed())
                    postsAdapter.notifyDataSetChanged()
                }
                ApiResponse.empty() -> setLoading(ApiResponseType.EMPTY)
                ApiResponse.error(apiResponse.message) ->{
                    setLoading(ApiResponseType.ERROR)
                    _binding.viewServerError.buttonConnect.setOnClickListener {
                        exitProcess(0)
                    }
                }

            }
        }

    }

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    private fun setUpUI() {
        activity?.actionBar?.hide()
//        val postsAdapter = PostsAdapter()
        val preferences = LoginPreference(this.requireContext())
        val userId = preferences.getUsername().toString()
        val name = preferences.getName().toString()
        val refreshView = RefreshView()

        binding.btAddPost.setOnClickListener {
            findNavController().navigate(R.id.postActivity)
        }

        binding.btAddImage.setOnClickListener {
            Toast.makeText(
                context,
                "Dalam pengembangan, kamu bisa tombol tambah dibawah untuk memposting",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btAddEvent.setOnClickListener {
            Toast.makeText(
                context,
                "Dalam pengembangan, kamu bisa tombol tambah dibawah untuk memposting",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btAddVideo.setOnClickListener {
            Toast.makeText(
                context,
                "Dalam pengembangan, kamu bisa tombol tambah dibawah untuk memposting",
                Toast.LENGTH_SHORT
            ).show()
        }

        userViewModel.getUserById(userId).observe(viewLifecycleOwner){
            Glide.with(this).asBitmap()
                .load(PHOTO_URL + it.data?.photo)
                .error(R.drawable.ic_avatar)
                .into(binding.image)

        }

        postsAdapter.btCommentOnClick = { postResponse ->
            viewModel.insertComments(postResponse?.postId.toString(),preferences.getUsername().toString(), name, postResponse?.body.toString())
                .observe(viewLifecycleOwner){
                    setUpViewModel()
                    Toast.makeText(this.context, "Komentar ditambahkan", Toast.LENGTH_SHORT).show()
                }
        }

        postsAdapter.btDislikeOnClick = {
            viewModel.deleteLike(it?.postId.toString(), name).observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse) {
                    ApiResponse.success(apiResponse.data) -> {
                        setUpViewModel()
                        Toast.makeText(this.context, "Batal menyukai", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        postsAdapter.btLikeOnClick = {
            viewModel.insertLike(it?.postId.toString(), name).observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse) {
                    ApiResponse.success(apiResponse.data) -> {
                        setUpViewModel()
                        Toast.makeText(this.context, "Disukai", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.rvPosts.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = postsAdapter
        }

    }

    private fun setLoading(apiResponseType: ApiResponseType) {
        when(apiResponseType){
            ApiResponseType.SUCCESS -> {
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.VISIBLE
                _binding.noPost.viewNoPost.visibility = View.GONE
                _binding.viewServerError.layoutServerDown.visibility = View.GONE
                _binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.EMPTY -> {
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                _binding.noPost.viewNoPost.visibility  = View.VISIBLE
                _binding.viewServerError.layoutServerDown.visibility = View.GONE
                _binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.ERROR ->{
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                _binding.noPost.viewNoPost.visibility  = View.GONE
                _binding.viewServerError.buttonConnect.visibility = View.INVISIBLE
                _binding.viewServerError.layoutServerDown.visibility = View.VISIBLE
                _binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.INTERNET_LOST ->{
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                _binding.noPost.viewNoPost.visibility = View.GONE
                _binding.viewServerError.layoutServerDown.visibility = View.GONE
                _binding.viewNoConnection.layoutNoConnection.visibility = View.VISIBLE
            }
        }

    }

//    @SuppressLint("NotifyDataSetChanged")
//    override fun onRefresh() {
//        setUpViewModel()
//        binding.swipeRefresh.isRefreshing = false
//    }


}
