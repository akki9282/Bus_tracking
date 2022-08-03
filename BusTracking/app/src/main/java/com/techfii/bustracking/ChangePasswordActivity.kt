package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityChangePasswordBinding
import com.techfii.bustracking.databinding.ActivityLoginBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.LoginViewModel

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    lateinit var preferences: SavePreferences
    lateinit var loginModel: LoginViewModel

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, ChangePasswordActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        loginModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        setViews()
        setListeners()
    }

    fun setViews() {
        if (preferences.getAdmin() == true) {
            binding.etAdmin.visibility = View.VISIBLE
            binding.etMobile.visibility = View.GONE
            binding.txtLabel.text = "Username"
        } else {
            binding.etAdmin.visibility = View.GONE
            binding.etMobile.visibility = View.VISIBLE
            binding.txtLabel.text = "Mobile No."
        }
    }

    fun setListeners() {

        binding.btnSubmit.setOnClickListener {
            var usertype = getUserType()
            var username = ""
            if (preferences.getAdmin() == true) {
                username = binding.etAdmin.text.toString()
                if (username.isNullOrEmpty()) {
                    binding.etAdmin.requestFocus()
                    Toast.makeText(this, "Please enter username", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            } else {
                username = binding.etMobile.text.toString()
                if (username.isNullOrEmpty()) {
                    binding.etMobile.requestFocus()
                    Toast.makeText(this, "Please enter mobile no", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                if (username.length != 10) {
                    binding.etMobile.requestFocus()
                    Toast.makeText(this, "Invalid Mobile no.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            var pwd = binding.oldPassword.text.toString()
            if (pwd.isNullOrEmpty()) {
                binding.oldPassword.requestFocus()
                Toast.makeText(this, "Please enter old password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var newpwd = binding.newPassword.text.toString()
            if (newpwd.isNullOrEmpty()) {
                binding.newPassword.requestFocus()
                Toast.makeText(this, "Please enter new password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.pg.visibility = View.VISIBLE
            binding.btnSubmit.visibility = View.GONE

            loginModel.changePassword(this, preferences.getUserid().toString(), newpwd, usertype)!!
                .observe(this,
                    Observer {
                        if (it != null) {
                            binding.pg.visibility = View.GONE
                            binding.btnSubmit.visibility = View.VISIBLE
                            if (it.status) {
                                preferences.setLogin(false)
                                Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
                                finish()
                                LoginActivity.startIntent(this)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Sorry,Could not change the password",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })

        }
    }

    fun getUserType(): String {
        var userType = ""
        if (preferences.getAdmin() == true) {
            userType = "admin"
        }
        if (preferences.getStudent() == true) {
            userType = "student"
        }
        if (preferences.getDriver() == true) {
            userType = "driver"
        }
        return userType
    }
}