package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityDriverFormBinding
import com.techfii.bustracking.databinding.ActivityDriverListBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.DriverViewModel
import com.techfii.bustracking.viewmodels.LoginViewModel

class DriverFormActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDriverFormBinding
    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, DriverFormActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)

        setListeners()
    }

    fun setListeners() {
        binding.btnCreateDriver.setOnClickListener {
            var driverName = binding.etDriverName.text.toString()
            var mobile = binding.etMobile.text.toString()
            var password = binding.etPassword.text.toString()
            if (driverName.isNullOrEmpty()) {
                binding.etDriverName.requestFocus()
                Toast.makeText(this, "Please enter driver name", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (mobile.isNullOrEmpty()) {
                binding.etMobile.requestFocus()
                Toast.makeText(this, "Please enter mobile no.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (mobile.length != 10) {
                binding.etMobile.requestFocus()
                Toast.makeText(this, "Invalid Mobile no.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (password.isNullOrEmpty()) {
                binding.etPassword.requestFocus()
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.pg.visibility = View.VISIBLE
            binding.btnCreateDriver.visibility = View.GONE
            driverModel.addDriver(this, driverName, password, mobile)!!.observe(this,
                Observer {
                    if (it != null) {
                        binding.pg.visibility = View.GONE
                        binding.btnCreateDriver.visibility = View.VISIBLE
                        if (it.status) {

                            binding.etDriverName.requestFocus()
                            binding.etDriverName.setText("")
                            binding.etMobile.setText("")
                            binding.etPassword.setText("")
                            Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this,   it.msg, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }
    }
}