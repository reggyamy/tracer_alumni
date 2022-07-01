package com.reggya.traceralumni

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.reggya.traceralumni.core.data.model.LoginResponse
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.databinding.ActivityLoginBinding
import com.reggya.traceralumni.ui.viewmodel.LoginViewModel
import com.reggya.traceralumni.ui.viewmodel.SurveyViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var surveyViewModel: SurveyViewModel
    private var mUsername : String? = null
    private var username = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isLogin()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.login.setOnClickListener(this)
        binding.btShowPwd.setOnClickListener(this)

    }

    private fun isLogin() {
        val preference = LoginPreference(this)
        mUsername = preference.getUsername()
        if (!preference.getIsLogin()){
            setUpUI()
        }else{
            checkSurvey(mUsername.toString())
        }
    }

    override fun onClick(v: View?) {
        username = binding.username.text.toString().trim()
        password = binding.password.text.toString().trim()

        when(v?.id) {
            R.id.login ->{
                when {
                    TextUtils.isEmpty(username) -> binding.username.error = resources.getString(R.string.error_et)
                    TextUtils.isEmpty(password) -> binding.password.error = resources.getString(R.string.error_et)
                    else ->{
                        setUpUI()
                    }
                }
            }
            R.id.bt_show_pwd ->{
                if(binding.password.transformationMethod.equals(PasswordTransformationMethod.getInstance())){
                    binding.btShowPwd.setImageResource(R.drawable.ic_eye_off)
                    //Show Password
                    binding.password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
                else{
                    binding.btShowPwd.setImageResource(R.drawable.ic_eye)
                    //Hide Password
                    binding.password.transformationMethod = PasswordTransformationMethod.getInstance()

                }
            }

        }

}

    private fun setUpUI() {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewModel.getUserLogin(username, password).observe(this) {
                when (it) {
                    ApiResponse.success(it.data) ->{
                        Toast.makeText(this, "Login sukses", Toast.LENGTH_SHORT).show()
                        loginSuccess(it.data)
                    }
                    ApiResponse.empty() -> Toast.makeText(this, "Akun tidak ditemukan \n cek username dan password", Toast.LENGTH_SHORT).show()
                    ApiResponse.error(it.message) ->
                        Toast.makeText(this, "Coba lagi", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun loginSuccess(data: LoginResponse?) {
        val preference = LoginPreference(this)
        if (data?.username != null) {
            preference.setLogin(data.username, data.name.toString())
            preference.isLogin(true)
            checkSurvey(data.username)
        }

    }

    private fun checkSurvey(username: String) {
        val surveyPreference = SurveyPreferences(this)
        val isSurveyCompleted = surveyPreference.getCompleted()
        val factory = ViewModelFactory.getInstance(this)
        surveyViewModel = ViewModelProvider(this, factory)[SurveyViewModel::class.java]
        if(!isSurveyCompleted){
//            val username = mUsername
            surveyViewModel.isSurveyCompleted(username)
                .observe(this){
                    when(it){
                        ApiResponse.success(it.data) -> {
                            if(it.data?.message == 0){
                                surveyPreference.isCompleted(false)
                                transaction()
                            }else{
                                surveyPreference.isCompleted(true)
                                transaction()
                            }
                        }
                    }
                }
        }else{
            surveyPreference.isCompleted(true)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun transaction() {
        startActivity(Intent(this, LandingActivity::class.java))
        finish()
    }
}


