package com.reggya.traceralumni

import android.content.AbstractThreadedSyncAdapter
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
import com.reggya.traceralumni.databinding.ActivityLoginBinding
import com.reggya.traceralumni.ui.viewmodel.LoginViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.ui.viewmodel.SurveyViewModel
import org.w3c.dom.Text


class LoginActivity : AppCompatActivity(), View.OnClickListener {

    companion object{
        const val KEY_USERNAME = "reggya"
        const val KEY_PASSWORD = "12345"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var surveyViewModel: SurveyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        surveyViewModel = ViewModelProvider(this, factory)[SurveyViewModel::class.java]

        isLogin()
        binding.login.setOnClickListener(this)
        binding.btShowPwd.setOnClickListener(this)

    }

    private fun isLogin() {
        val preference = LoginPreference(this)
        val surveyPreference = SurveyPreferences(this)
        if (preference.getIsLogin()){
            if(surveyPreference.getCompleted()){
                surveyPreference.isCompleted(true)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                surveyViewModel.isSurveyCompleted(preference.getUsername().toString())
                    .observe(this){
                        when(it){
                            ApiResponse.success(it.data) -> {
                                if(it.data?.message == 0){
                                    startActivity(Intent(this, LandingActivity::class.java))
                                    finish()
                                }else{
                                    surveyPreference.isCompleted(true)
                                    startActivity(Intent(this, MainActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onClick(v: View?) {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        when(v?.id) {
            R.id.login ->{
                when {
                    TextUtils.isEmpty(username) -> binding.username.error = resources.getString(R.string.error_et)
                    TextUtils.isEmpty(password) -> binding.password.error = resources.getString(R.string.error_et)
                    else -> setUpUI(username, password)
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

    private fun setUpUI(username: String, password: String) {

        viewModel.getUserLogin(username, password).observe(this) {
            when (it) {
                ApiResponse.success(it.data) ->
                    loginSuccess(it.data)
                ApiResponse.error(it.message) ->
                    Toast.makeText(this, "Coba lagi", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginSuccess(data: LoginResponse?) {
        val preference = LoginPreference(this)
        if (data?.username != null) {
            preference.setLogin(data.username, data.name.toString())
            preference.isLogin(true)

            val intent = Intent(this, LandingActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}


