package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityLoginBinding
import com.techfii.bustracking.databinding.ActivityUserSelectionBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    val TAG = "LOGIN"
    private lateinit var binding: ActivityLoginBinding
    lateinit var preferences: SavePreferences
    lateinit var loginModel: LoginViewModel

    companion object {
        fun startIntent(context: Context) {

            val i = Intent(context, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)


        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.btnLogin.setOnClickListener {
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
            var pwd = binding.etPassword.text.toString()
            if (pwd.isNullOrEmpty()) {
                binding.etPassword.requestFocus()
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.pg.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
            loginModel.getAdminLogin(this, username, pwd, usertype)!!.observe(this,
                Observer {
                    if (it != null) {
                        binding.pg.visibility = View.GONE
                        binding.btnLogin.visibility = View.VISIBLE

                        if (it.status) {
                            preferences.setLogin(it.status)
                            preferences.setUserid(it.id)
                            goToActivity()
                        } else {
                            Toast.makeText(
                                this,
                                it.msg,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })

        }
    }

    fun goToActivity() {
        if (preferences.getAdmin() == true) {
            AdminActivity.startIntent(this)
        }
        if (preferences.getStudent() == true) {
            StudentActivity.startIntent(this)
        }
        if (preferences.getDriver() == true) {
            DriverTrackingActivity.startIntent(this)
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