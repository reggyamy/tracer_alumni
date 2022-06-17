package com.reggya.traceralumni

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import androidx.navigation.navArgument
import com.github.dhaval2404.imagepicker.ImagePicker
import com.reggya.traceralumni.ProfileFragment.Companion.REQUEST_PERMISSION
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.databinding.ActivityPostBinding
import com.reggya.traceralumni.ui.viewmodel.PostViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import com.reggya.traceralumni.core.utils.LoginPreference
import java.io.File

class PostActivity : AppCompatActivity(){

    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel

    private var description = ""
    private var link = ""
    private var image: File? = null

    companion object{
        const val EXTRA_POST_IMAGE = "file post image"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSubmit.setOnClickListener {
            description = binding.captions.text.toString()
            setObserver(description)
        }
        binding.btCamera.setOnClickListener {
            checkCameraPermission()
            onCameraTap()
        }

    }

    private fun onCameraTap() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(150, 150)
            .cropSquare()
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_PERMISSION
            )
        }
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val fileUri = data?.data
                binding.imageView.setImageURI(fileUri)

                binding.frameCamera.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
                
                image = fileUri?.toFile()
//                setObserver()
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun setObserver(description: String) {
        val preference = LoginPreference(this)
        val user_id = preference.getUsername()

        Toast.makeText(this, "123", Toast.LENGTH_SHORT).show()
//        description = binding.captions.text.toString()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[PostViewModel::class.java]

        if (user_id != null) {
            viewModel.insertPost("d400180132", image, description,link).observe(this) {
                when (it) {
                    ApiResponse.success(it.data) -> {
                        Toast.makeText(this, "222", Toast.LENGTH_SHORT).show()
                        showProgressBar()
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.progress_bar)
//
//        if (dialog.window != null) dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        dialog.show()

        val dialog = AlertDialog.Builder(this, R.style.AlertDialogTheme)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.progress_bar, null)
        dialog.setView(view)

//        val tvTitle = view.findViewById<TextView>(R.id.tv_alert_dialog1)
//        tvTitle.text = title

        val alertDialog = dialog.create()
//        alertBinding.btSave.setOnClickListener {
//            val newData = alertBinding.input.text.toString().trim()
//            if (title == getString(R.string.title_ad_alumni)) setData(newData,"","","","")
//            else setData("", newData,"","","")
//        }
//
//        alertBinding.btCancel.setOnClickListener {
//            dialog.create().dismiss()
//        }

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
    }

}