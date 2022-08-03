package com.techfii.bustracking.utilities

import android.content.Context
import android.content.SharedPreferences
import com.techfii.bustracking.R

class SavePreferences(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {

        const val DRIVER_FLAG = "driver_flag"
        const val STUDENT_FLAG = "student_flag"
        const val ADMIN_FLAG = "admin_flag"
        const val LOGIN = "Login"
        const val USERID = "Userid"
        const val JOURNEY_ID= "journey_id"

    }




    fun setDriver(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(DRIVER_FLAG, value)
        editor.apply()
    }


    fun getDriver(): Boolean? {
        return prefs.getBoolean(DRIVER_FLAG, false)
    }

    fun setStudent(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(STUDENT_FLAG, value)
        editor.apply()
    }


    fun getStudent(): Boolean? {
        return prefs.getBoolean(STUDENT_FLAG, false)
    }


    fun setAdmin(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(ADMIN_FLAG, value)
        editor.apply()
    }
    fun getAdmin(): Boolean? {
        return prefs.getBoolean(ADMIN_FLAG, false)
    }
    fun setLogin(value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }
    fun getLogin(): Boolean? {
        return prefs.getBoolean(LOGIN, false)
    }

    fun setUserid(value: String) {
        val editor = prefs.edit()
        editor.putString(USERID, value)
        editor.apply()
    }
    fun getUserid(): String? {
        return prefs.getString(USERID, "")
    }

    fun setJourneyid(value: String) {
        val editor = prefs.edit()
        editor.putString(JOURNEY_ID, value)
        editor.apply()
    }
    fun getJourneyid(): String? {
        return prefs.getString(JOURNEY_ID, "")
    }
}