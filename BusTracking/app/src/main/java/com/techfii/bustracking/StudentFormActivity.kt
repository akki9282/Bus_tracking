package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityBusFormBinding
import com.techfii.bustracking.databinding.ActivityStudentFormBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.BusViewModel
import com.techfii.bustracking.viewmodels.DriverViewModel
import com.techfii.bustracking.viewmodels.StudentViewModel

class StudentFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentFormBinding
    lateinit var preferences: SavePreferences
    lateinit var studentModel: StudentViewModel

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, StudentFormActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        studentModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        setListeners()
    }

    fun setListeners() {
        binding.btnCreateStudent.setOnClickListener {
            var studentName = binding.etName.text.toString()
            var mobile = binding.etMobile.text.toString()
            var password = binding.etPassword.text.toString()
            var standard = binding.etStandard.text.toString()
            var address = binding.etAddress.text.toString()

            if (studentName.isNullOrEmpty()) {
                binding.etName.requestFocus()
                Toast.makeText(this, "Please enter student name", Toast.LENGTH_LONG).show()
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
            if (standard.isNullOrEmpty()) {
                binding.etStandard.requestFocus()
                Toast.makeText(this, "Please enter standard", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (address.isNullOrEmpty()) {
                binding.etAddress.requestFocus()
                Toast.makeText(this, "Please enter address", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.pg.visibility = View.VISIBLE
            binding.btnCreateStudent.visibility = View.GONE
            studentModel.addStudent(this, studentName, mobile, password, standard, address)!!
                .observe(this,
                    Observer {
                        if (it != null) {
                            binding.pg.visibility = View.GONE
                            binding.btnCreateStudent.visibility = View.VISIBLE
                            if (it.status) {
                                binding.etName.requestFocus()
                                binding.etName.setText("")
                                binding.etMobile.setText("")
                                binding.etPassword.setText("")
                                binding.etStandard.setText("")
                                binding.etAddress.setText("")
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