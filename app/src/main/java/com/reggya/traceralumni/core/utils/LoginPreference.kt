package com.reggya.traceralumni.core.utils

import android.content.Context

internal class LoginPreference(context: Context) {

    companion object{
        private const val KEY_PREFS = "user_pref"
        const val KEY_LOGIN_USERNAME = "username"
        const val KEY_LOGIN_NAME = "name"
        private const val ISLOGIN = "false"
    }

    private val preference = context.getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)

    fun setLogin(username : String, name: String){
        val editor = preference.edit()
        editor.putString(KEY_LOGIN_USERNAME, username)
        editor.putString(KEY_LOGIN_NAME, name)
        editor.apply()
    }

    fun getUsername() = preference.getString(KEY_LOGIN_USERNAME, "")

    fun getName()= preference.getString(KEY_LOGIN_NAME, "")

    fun isLogin(state : Boolean){
        val editor = preference.edit()
        editor.putBoolean(ISLOGIN, state)
            .apply()
    }

    fun getIsLogin(): Boolean{
        return preference.getBoolean(ISLOGIN, false)
    }
}