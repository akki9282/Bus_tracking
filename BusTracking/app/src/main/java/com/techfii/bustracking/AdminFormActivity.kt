package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityAdminFormBinding
import com.techfii.bustracking.databinding.ActivityStudentFormBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.AdminViewModel
import com.techfii.bustracking.viewmodels.StudentViewModel

class AdminFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminFormBinding
    lateinit var preferences: SavePreferences
    lateinit var adminViewModel: AdminViewModel

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, AdminFormActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)

        setListeners()
    }

    fun setListeners() {
        binding.btnCreateAdmin.setOnClickListener {
            var adminname = binding.etName.text.toString()
            var mobile = binding.etMobile.text.toString()
            var password = binding.etPassword.text.toString()


            if (adminname.isNullOrEmpty()) {
                binding.etName.requestFocus()
                Toast.makeText(this, "Please enter admin name", Toast.LENGTH_LONG).show()
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
            binding.btnCreateAdmin.visibility = View.GONE
            adminViewModel.addAdmin(this, adminname, mobile, password)!!
                .observe(this,
                    Observer {
                        if (it != null) {
                            binding.pg.visibility = View.GONE
                            binding.btnCreateAdmin.visibility = View.VISIBLE
                            if (it.status) {
                                binding.etName.requestFocus()
                                binding.etName.setText("")
                                binding.etMobile.setText("")
                                binding.etPassword.setText("")
                                Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
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
}