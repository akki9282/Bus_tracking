package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityAdminBinding
import com.techfii.bustracking.databinding.ActivityLoginBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.BusViewModel
import com.techfii.bustracking.viewmodels.LoginViewModel

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    lateinit var preferences: SavePreferences
    lateinit var loginModel: LoginViewModel

    companion object {
        fun startIntent(context: Context) {

            val i = Intent(context, AdminActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        loginModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setListeners()
    }

    fun logout() {
        loginModel.userlogout(this, preferences.getUserid().toString(), "admin")!!.observe(this,
            Observer {
                if (it != null) {


                    if (it.status) {
                        preferences.setLogin(false)
                        UserSelectionActivity.startIntent(this)

                        Toast.makeText(
                            this,
                            "Could not logout",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Could not logout",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun setListeners() {
        binding.ivChangePwd.setOnClickListener {
            ChangePasswordActivity.startIntent(this)
        }
        binding.ivLogout.setOnClickListener {
            logout()
        }
        binding.frmDriver.setOnClickListener {
            DriverListActivity.startIntent(this)
        }
        binding.frmBus.setOnClickListener {
            BusListActivity.startIntent(this)
        }
        binding.frmStudent.setOnClickListener {
            StudentListActivity.startIntent(this)
        }
        binding.frmAllocation.setOnClickListener {
            BusAllocationActivity.startIntent(this)
        }
        binding.frmAdmin.setOnClickListener {
            AdminListActivity.startIntent(this)
        }
    }
}