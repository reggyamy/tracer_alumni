package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.reggya.traceralumni.BuildConfig.PHOTO_URL
import com.reggya.traceralumni.PostActivity.Companion.EXTRA_POST_IMAGE
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.data.remote.ApiResponseType
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.ContentHomeBinding
import com.reggya.traceralumni.ui.ConnectionLiveData
import com.reggya.traceralumni.ui.RefreshView
import com.reggya.traceralumni.ui.adapter.PostsAdapter
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ProfileViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import kotlin.system.exitProcess


class HomeFragment : Fragment() {

    private lateinit var binding : ContentHomeBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var userViewModel: ProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = ContentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkInternetConnection()){
            setUpViewModel()
            setUpUI()
        }else{
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

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = ConnectionLiveData()
        return connectivityManager.checkInternetConnection(this.requireContext())
    }

    private fun setUpViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
        userViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged", "InflateParams")
    private fun setUpUI() {
        activity?.actionBar?.hide()
        val postsAdapter = PostsAdapter()
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
            Glide.with(this)
                .load(PHOTO_URL + it.data?.photo)
                .error(R.drawable.ic_avatar)
                .into(binding.image)
        }

        viewModel.getAllPost().observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                ApiResponse.success(apiResponse.data) ->{
                    refreshView.refresh(15000)
                    setLoading(ApiResponseType.SUCCESS)
                    postsAdapter.setNewData(apiResponse.data?.reversed())
                    postsAdapter.notifyDataSetChanged()
                }
                ApiResponse.empty() -> setLoading(ApiResponseType.EMPTY)
                ApiResponse.error(apiResponse.message) ->{
                    setLoading(ApiResponseType.ERROR)
                    binding.viewServerError.buttonConnect.setOnClickListener {
                        exitProcess(0)
                    }
                }

            }
        }

        postsAdapter.btCommentOnClick = { postResponse ->
            viewModel.insertComments(postResponse?.postId.toString(), postResponse?.userId.toString(), name, postResponse?.body.toString())
                .observe(viewLifecycleOwner){
                    refreshView.refresh(1000)
                    Toast.makeText(this.context, "Komentar ditambahkan", Toast.LENGTH_SHORT).show()
                }
        }

        postsAdapter.btDislikeOnClick = {
            viewModel.deleteLike(it?.postId.toString(), name).observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse) {
                    ApiResponse.success(apiResponse.data) -> {
                        refreshView.refresh(1000)
                        Toast.makeText(this.context, "Batal menyukai", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        postsAdapter.btLikeOnClick = {
            viewModel.insertLike(it?.postId.toString(), name).observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse) {
                    ApiResponse.success(apiResponse.data) -> {
                        refreshView.refresh(1000)
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
                binding.noPost.viewNoPost.visibility = View.GONE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.EMPTY -> {
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                binding.noPost.viewNoPost.visibility  = View.VISIBLE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.ERROR ->{
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                binding.noPost.viewNoPost.visibility  = View.GONE
                binding.viewServerError.buttonConnect.visibility = View.INVISIBLE
                binding.viewServerError.layoutServerDown.visibility = View.VISIBLE
                binding.viewNoConnection.layoutNoConnection.visibility = View.GONE
            }
            ApiResponseType.INTERNET_LOST ->{
                binding.shimmer.visibility = View.GONE
                binding.rvPosts.visibility = View.GONE
                binding.noPost.viewNoPost.visibility = View.GONE
                binding.viewServerError.layoutServerDown.visibility = View.GONE
                binding.viewNoConnection.layoutNoConnection.visibility = View.VISIBLE
            }
        }

    }



}
