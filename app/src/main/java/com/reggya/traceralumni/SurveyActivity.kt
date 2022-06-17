package com.reggya.traceralumni

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.TextUtilsCompat
import androidx.lifecycle.ViewModelProvider
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.databinding.ActivitySurveyBinding
import com.reggya.traceralumni.databinding.QuestionsSurveyBinding
import com.reggya.traceralumni.ui.viewmodel.SurveyViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory
import org.w3c.dom.Text


class SurveyActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySurveyBinding
    private lateinit var _binding : QuestionsSurveyBinding
    private lateinit var viewModel : SurveyViewModel
    private var status : String? = null
    private var name : String? = null
    private var id : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        _binding = binding.survey
        setContentView(binding.root)

        val preference = LoginPreference(this)
        id = preference.getUsername()
        name = preference.getName()

        _binding.name.text = name.toString()
        _binding.studentId.text = id.toString()
        _binding.rbTrue.setOnClickListener {
            _binding.viewJobInformation.visibility = View.VISIBLE
        }
        _binding.rbFalse.setOnClickListener {
            _binding.viewJobInformation.visibility = View.GONE
        }



        _binding.buttonSubmit.setOnClickListener {
            val major = _binding.major.text.toString().trim()
            val yearOfEntry = _binding.yearOfEntry.text.toString().trim()
            val graduationYear = _binding.graduationYear.text.toString().trim()
            val gpa = _binding.gpa.text.toString().trim()
            val company = _binding.company.text.toString().trim()
            val companyAddress = _binding.companyAddress.text.toString().trim()
            val position = _binding.position.text.toString().trim()
            val yearOfWork = _binding.position.text.toString().trim()
            val salary = _binding.salary.text.toString().trim()
            val feedback = _binding.feedback.text.toString().trim()
            val genderRadioGroup =_binding.rb
            val selectedRadioButton: Int = genderRadioGroup.checkedRadioButtonId
            val radioButton = findViewById<RadioButton>(selectedRadioButton)
            val status = radioButton.text.toString()

            when{
                TextUtils.isEmpty(major) -> _binding.major.error = resources.getString(R.string.error_et)
                TextUtils.isEmpty(yearOfEntry) -> _binding.yearOfEntry.error = resources.getString(R.string.error_et)
                TextUtils.isEmpty(graduationYear) -> _binding.graduationYear.error = resources.getString(R.string.error_et)
                TextUtils.isEmpty(gpa) -> _binding.gpa.error = resources.getString(R.string.error_et)
                TextUtils.isEmpty(feedback) -> _binding.feedback.error = resources.getString(R.string.error_et)
                else ->{
                    if (status.isNotEmpty()){
                        if(status == resources.getString(R.string.sudah_bekerja)){
                            when{
                                TextUtils.isEmpty(company) -> _binding.company.error = resources.getString(R.string.error_et)
                                TextUtils.isEmpty(companyAddress) -> _binding.companyAddress.error = resources.getString(R.string.error_et)
                                TextUtils.isEmpty(position) -> _binding.position.error = resources.getString(R.string.error_et)
                                TextUtils.isEmpty(yearOfWork) -> _binding.position.error = resources.getString(R.string.error_et)
                                TextUtils.isEmpty(salary) ->_binding.salary.error = resources.getString(R.string.error_et)
                            }
                        }
                        setViewModel(id.toString(), name.toString(), major, yearOfEntry, graduationYear, gpa,
                            status, company, companyAddress, position, yearOfWork, salary, feedback)
                    }

                }
            }
        }

    }

    private fun setViewModel(
        id: String,
        name: String,
        major: String,
        yearOfEntry: String,
        graduationYear: String,
        gpa: String,
        status: String,
        company: String,
        companyAddress: String,
        position: String,
        yearOfWork: String,
        salary: String,
        feedback: String
    ) {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[SurveyViewModel::class.java]

        viewModel.getSurveyUser(id,name, major, yearOfEntry, graduationYear, gpa, status, company, companyAddress, position, yearOfWork, salary, feedback)
            .observe(this) {
                when (it) {
                    ApiResponse.success(it.data) -> {
                        val preferences = SurveyPreferences(this)
                        preferences.isCompleted(true)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
    }

}
