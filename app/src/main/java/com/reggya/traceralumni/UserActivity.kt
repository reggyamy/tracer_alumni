package com.reggya.traceralumni

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgument
import com.reggya.traceralumni.R
import com.reggya.traceralumni.core.data.remote.ApiResponseType
import com.reggya.traceralumni.databinding.ActivityUserBinding
import com.reggya.traceralumni.ui.ConnectionLiveData
import com.reggya.traceralumni.ui.ImagePicker
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ProfileViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkInternetConnection()){
            setUpViewModel()
            setUpUI()
        }else{
            setLoading(ApiResponseType.INTERNET_LOST)
            binding.noConnection.buttonConnect.setOnClickListener {
                checkInternetConnection()
                if (checkInternetConnection()) {
                    setLoading(ApiResponseType.SUCCESS)
                    Toast.makeText(this, this.getString(R.string.connected), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, this.getString(R.string.not_connected), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun setUpViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        postViewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]
    }

    private fun setUpUI() {
        navArgument()
        userViewModel.getUserById()
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = ConnectionLiveData()
        return connectivityManager.checkInternetConnection(this)
    }

    private fun setLoading(apiResponseType: ApiResponseType) {
        TODO("Not yet implemented")
    }

}