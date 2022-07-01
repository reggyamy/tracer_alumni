package com.reggya.traceralumni

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.reggya.traceralumni.core.data.remote.ApiResponse
import com.reggya.traceralumni.core.utils.LoginPreference
import com.reggya.traceralumni.core.utils.SurveyPreferences
import com.reggya.traceralumni.databinding.ActivitySurveyBinding
import com.reggya.traceralumni.databinding.QuestionsSurveyBinding
import com.reggya.traceralumni.ui.viewmodel.SurveyViewModel
import com.reggya.traceralumni.ui.viewmodel.ViewModelFactory


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
            var company = _binding.company.text.toString().trim()
            var companyAddress = _binding.companyAddress.text.toString().trim()
            var position = _binding.position.text.toString().trim()
            var yearOfWork = _binding.yearWork.text.toString().trim()
            var salary = _binding.salary.text.toString().trim()
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
                                TextUtils.isEmpty(yearOfWork) -> _binding.yearWork.error = resources.getString(R.string.error_et)
                                TextUtils.isEmpty(salary) ->_binding.salary.error = resources.getString(R.string.error_et)
                                else ->{
                                    setViewModel(id.toString(), name.toString(), major, yearOfEntry, graduationYear, gpa,
                                        status, company, companyAddress, position, yearOfWork, salary, feedback)
                                }
                            }
                        }else{
                            company = ""
                            companyAddress = ""
                            position = ""
                            yearOfWork = ""
                            salary = ""
                            setViewModel(id.toString(), name.toString(), major, yearOfEntry, graduationYear, gpa,
                                status, company, companyAddress, position, yearOfWork, salary, feedback)
                        }
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
        val preferences = SurveyPreferences(this)
        viewModel.getSurveyUser(id,name, major, yearOfEntry, graduationYear, gpa, status, company, companyAddress, position, yearOfWork, salary, feedback)
            .observe(this) {
                when (it) {
                    ApiResponse.success(it.data) -> {
                        preferences.isCompleted(true)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
    }

}
