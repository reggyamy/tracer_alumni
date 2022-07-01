package com.reggya.traceralumni

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.reggya.traceralumni.BuildConfig.PHOTO_URL
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.databinding.ActivityUserBinding
import com.reggya.traceralumni.ui.ConnectionLiveData
import com.reggya.traceralumni.ui.adapter.PostHistoryAdapter
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ProfileViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: ProfileViewModel

    companion object{
        const val EXTRA_ID = "user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        checkInternetConnection()

    }

    private fun setUpViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        postViewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpUI() {
        val postHistoryAdapter = PostHistoryAdapter()
        val userId = intent.getStringExtra(EXTRA_ID)
        binding.btChat.setOnClickListener {
            Toast.makeText(this, "On development", Toast.LENGTH_SHORT).show()
        }
        userViewModel.getUserById(userId.toString()).observe(this){
            val data = it.data
            when(it){
                ApiResponse.success(data) ->{
                    binding.name.text = data?.name
                    binding.job.text = data?.job
                    binding.address.text = data?.address
                    binding.major.text = data?.alumni
                    binding.about.text = data?.about
                    Glide.with(this)
                        .load(PHOTO_URL+data?.photo)
                        .error(R.drawable.ic_avatar)
                        .into(binding.image)
                }
                ApiResponse.error(it.message) ->{
                    binding.serverDown.layoutServerDown.visibility = View.VISIBLE
                    binding.serverDown.buttonConnect.setOnClickListener { view ->
                        view.findNavController().navigate(R.id.navigation_home)
                    }
                }
            }
        }

        postViewModel.getPostsByUserId(userId.toString()).observe(this) {
            when (it) {
                ApiResponse.success(it.data) -> {
                    binding.rvPostHistory.visibility = View.VISIBLE
                    postHistoryAdapter.setData(it.data?.reversed())
                    postHistoryAdapter.notifyDataSetChanged()
                }
                ApiResponse.empty() -> {
                    binding.tvNopost.visibility = View.VISIBLE
                }
                ApiResponse.error(it.message) -> {
                    binding.serverDown.layoutServerDown.visibility = View.VISIBLE
                    binding.serverDown.buttonConnect.setOnClickListener { view ->
                        view.findNavController().popBackStack()
                    }
                }
            }
        }

        binding.rvPostHistory.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = postHistoryAdapter
        }
    }

    private fun checkInternetConnection(){
        val cld = ConnectionLiveData(this)
        cld.observe(this){isConnected ->
            if (isConnected){
                setUpViewModel()
                setUpUI()
            }else {
                binding.noConnection.layoutNoConnection.visibility = View.VISIBLE
                binding.noConnection.buttonConnect.setOnClickListener {
                    cld.observe(this) { isConnected ->
                        if (isConnected) {
                            binding.noConnection.layoutNoConnection.visibility = View.GONE
                            Toast.makeText(
                                this,
                                this.getString(R.string.connected),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            binding.noConnection.layoutNoConnection.visibility = View.VISIBLE
                            Toast.makeText(
                                this,
                                this.getString(R.string.not_connected),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}