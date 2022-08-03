package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.techfii.bustracking.databinding.ActivityUserSelectionBinding
import com.techfii.bustracking.utilities.SavePreferences

class UserSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSelectionBinding
    lateinit var preferences: SavePreferences
    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, UserSelectionActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        preferences.setDriver(false)
        preferences.setStudent(false)
        preferences.setAdmin(false)
        setListeners()
    }

    fun setListeners() {

        binding.frmDriver.setOnClickListener {
            binding.frmDriver.setBackgroundResource(R.drawable.bg_user_selection)
            binding.frmStudent.setBackgroundResource(R.drawable.bg_user_notselected)
            binding.frmAdmin.setBackgroundResource(R.drawable.bg_user_notselected)

            binding.radioDriver.visibility = View.VISIBLE
            binding.radioStudent.visibility = View.GONE
            binding.radioAdmin.visibility = View.GONE
            preferences.setDriver(true)
            preferences.setStudent(false)
            preferences.setAdmin(false)
            gotoLoginScreen()
        }

        binding.frmStudent.setOnClickListener {
            binding.frmDriver.setBackgroundResource(R.drawable.bg_user_notselected)
            binding.frmStudent.setBackgroundResource(R.drawable.bg_user_selection)
            binding.frmAdmin.setBackgroundResource(R.drawable.bg_user_notselected)

            binding.radioDriver.visibility = View.GONE
            binding.radioStudent.visibility = View.VISIBLE
            binding.radioAdmin.visibility = View.GONE
            preferences.setDriver(false)
            preferences.setStudent(true)
            preferences.setAdmin(false)
            gotoLoginScreen()
        }
        binding.frmAdmin.setOnClickListener {
            binding.frmDriver.setBackgroundResource(R.drawable.bg_user_notselected)
            binding.frmStudent.setBackgroundResource(R.drawable.bg_user_notselected)
            binding.frmAdmin.setBackgroundResource(R.drawable.bg_user_selection)

            binding.radioDriver.visibility = View.GONE
            binding.radioStudent.visibility = View.GONE
            binding.radioAdmin.visibility = View.VISIBLE
            preferences.setDriver(false)
            preferences.setStudent(false)
            preferences.setAdmin(true)
            gotoLoginScreen()
        }
    }

    fun gotoLoginScreen(){
        Handler().postDelayed({
            LoginActivity.startIntent(this)
        }, 500)
    }
}