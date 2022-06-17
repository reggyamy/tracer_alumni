package com.reggya.traceralumni.core.utils

import android.content.Context

internal class SurveyPreferences(context: Context) {

    companion object{
        private const val KEY_SURVEY_PREF = "survey preference"
        private const val KEY_SURVEY = "survey"
            private const val ISCOMPLETED = "false"
    }

    private val surveyPreferences = context.getSharedPreferences(KEY_SURVEY_PREF, Context.MODE_PRIVATE)

    fun isCompleted(state: Boolean){
        val editor = surveyPreferences.edit()
        editor.putBoolean(ISCOMPLETED, state)
            .apply()
    }

    fun getCompleted(): Boolean{
        return surveyPreferences.getBoolean(ISCOMPLETED, false)
    }
}