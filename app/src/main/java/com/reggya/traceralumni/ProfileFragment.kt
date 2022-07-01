package com.reggya.traceralumni

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.databinding.FragmentProfileBinding
import com.reggya.traceralumni.ui.RefreshView
import com.reggya.traceralumni.ui.viewmodel.ProfileViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import java.io.File


class ProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var alumni : String? = ""
    private var job : String? = ""
    private var company : String? = ""
    private var address : String? = ""
    private var about : String? = ""
    private var password : String? = ""
    private var photo : File? = null
    private var image: String? = ""
    private var userId: String? = ""
    private lateinit var refresh: RefreshView


    companion object{
        const val REQUEST_PERMISSION = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
        refresh = RefreshView()

        val preferences = LoginPreference(requireContext())
        userId = preferences.getUsername()

        binding.name.text = preferences.getName()
        viewModel.getUserById(userId.toString()).observe(viewLifecycleOwner) { apiResponse ->
                when (apiResponse) {
                    ApiResponse.success(apiResponse.data) -> {
                        image = apiResponse.data?.photo
                        alumni = apiResponse.data?.alumni
                        job = apiResponse.data?.job
                        address = apiResponse.data?.address
                        about = apiResponse.data?.about
                        password = apiResponse.data?.password
                        binding.job.text = job
                        binding.address.text = address
                        binding.alumni.text = alumni
                        binding.about.text = about
                        Glide.with(this).load(BuildConfig.PHOTO_URL +image)
                            .error(R.drawable.ic_avatar).into(binding.photo)
                    }
                }
        }

        binding.btAddPict.setOnClickListener(this)
        binding.addAlumni.setOnClickListener(this)
        binding.addAddress.setOnClickListener(this)
        binding.addJob.setOnClickListener(this)
        binding.addAbout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.bt_add_pict -> {
                val imagePicker = com.reggya.traceralumni.ui.ImagePicker(this.requireActivity())
                imagePicker.checkCameraPermission()
                onCameraTap()
            }
            R.id.add_alumni -> alertDialog(resources.getString(R.string.title_ad_alumni))
            R.id.add_address -> alertDialog(resources.getString(R.string.title_ad_address))
            R.id.add_job -> alertDialog2()
            R.id.add_about -> alertDialog(resources.getString(R.string.title_ad_about))
        }
    }

    private fun alertDialog2() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog2, null)
        dialog.setView(view)
        val submit = view.findViewById<AppCompatButton>(R.id.button_submit)
        val cancel = view.findViewById<AppCompatButton>(R.id.button_cancel)
        val tvTitle1 = view.findViewById<TextView>(R.id.tv_title1)
        val tvTitle2 = view.findViewById<TextView>(R.id.tv_title2)
        val etJob = view.findViewById<EditText>(R.id.input_job)
        val etCompany = view.findViewById<EditText>(R.id.input_company)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tvTitle1.text = resources.getString(R.string.job_position)
        tvTitle2.text = resources.getString(R.string.name_company)


        submit.setOnClickListener {
            val job_ = etJob.text.toString().trim()
            val company_ = etCompany.text.toString().trim()
            job = "$job_, $company_"
            val data ="$job_, $company_"
            binding.job.text = data
            setUpdateProfile()
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun alertDialog(title: String) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val view = layoutInflater.inflate(R.layout.alert_dialog1, null)
        dialog.setView(view)
        val tvTitle = view.findViewById<TextView>(R.id.tv_alert_dialog1)
        val submit = view.findViewById<AppCompatButton>(R.id.bt_save)
        val cancel = view.findViewById<AppCompatButton>(R.id.bt_cancel)
        val data = view.findViewById<EditText>(R.id.input)
        tvTitle.text = title
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        submit.setOnClickListener {
            val newData = data.text.toString()
            when (title) {
                resources.getString(R.string.title_ad_alumni) -> {
                    binding.alumni.text = newData
                    alumni = newData
                    Toast.makeText(this.requireContext(), newData, Toast.LENGTH_SHORT).show()
                    setUpdateProfile()
                }
                resources.getString(R.string.title_ad_about) -> {
                    binding.about.text = newData
                    about = newData
                    Toast.makeText(this.requireContext(), newData, Toast.LENGTH_SHORT).show()
                    setUpdateProfile()
                }
                resources.getString(R.string.title_ad_address) -> {
                    binding.address.text = newData
                    address = newData
                    setUpdateProfile()
                    Toast.makeText(this.requireContext(), newData, Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setUpdatePhoto() {
        viewModel.getUploadImage(userId.toString(), photo!!).observe(this){
            when(it){
                ApiResponse.success(it.data) ->{
                    Toast.makeText(
                        requireContext(),
                        "Yei, kamu berhasil update profilmu.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun setUpdateProfile() {
        viewModel.getUpdateProfile(userId,alumni, job, address, about, password).observe(viewLifecycleOwner) { apiResponse ->
            when (apiResponse) {
                ApiResponse.success(apiResponse.data) -> {
                    refresh.refresh(100)
                    Toast.makeText(requireContext(), "Yei, kamu berhasil update profilmu.", Toast.LENGTH_SHORT).show()
                }
            }
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

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data
                    if (fileUri != null) {
                        photo = fileUri.toFile()
                    }
                    setUpdatePhoto()
                    binding.photo.setImageURI(fileUri)
                }
                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }


}