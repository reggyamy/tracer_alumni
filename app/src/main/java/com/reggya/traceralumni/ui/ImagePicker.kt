package com.reggya.traceralumni.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import com.github.dhaval2404.imagepicker.ImagePicker
import com.reggya.traceralumni.ProfileFragment

class ImagePicker(private val activity: Activity) {

    fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                ProfileFragment.REQUEST_PERMISSION
            )
        }
    }

    fun onCameraTap(context: Context): Intent {
        var image = Intent(context, T::class.java)
       ImagePicker.with(activity)
            .crop()
            .compress(1024)
            .maxResultSize(150, 150)
            .cropSquare()
            .createIntent { intent ->
              image = intent
            }
        return image
    }



//    private val startForProfileImageResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//                val resultCode = result.resultCode
//                val data = result.data
//
//                when (resultCode) {
//                    Activity.RESULT_OK -> {
//                        //Image Uri will not be null for RESULT_OK
//                        val fileUri = data?.data
////                    if (fileUri != null) {
////                        val photo = fileUri.toFile()
////                    }
////                    setUpdatePhoto()
////                    binding.photo.setImageURI(fileUri)
//                    }
//                    ImagePicker.RESULT_ERROR -> {
//                        Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//                    }
//                    else -> {
//                        Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }

}