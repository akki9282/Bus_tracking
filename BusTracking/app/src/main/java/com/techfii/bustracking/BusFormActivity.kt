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
import com.techfii.bustracking.databinding.ActivityDriverFormBinding
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.BusViewModel
import com.techfii.bustracking.viewmodels.DriverViewModel

class BusFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBusFormBinding
    lateinit var preferences: SavePreferences
    lateinit var busModel: BusViewModel

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, BusFormActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        busModel = ViewModelProvider(this).get(BusViewModel::class.java)
        setListeners()
    }

    fun setListeners() {
        binding.btnCreateBus.setOnClickListener {
            var brand = binding.etBrand.text.toString()
            var busno = binding.etBusNo.text.toString()
            var collegeno = binding.etCollegeno.text.toString()
            if (brand.isNullOrEmpty()) {
                binding.etBrand.requestFocus()
                Toast.makeText(this, "Please enter bus brand", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (busno.isNullOrEmpty()) {
                binding.etBusNo.requestFocus()
                Toast.makeText(this, "Please enter bus no.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (collegeno.isNullOrEmpty()) {
                binding.etCollegeno.requestFocus()
                Toast.makeText(this, "Please enter college no.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.pg.visibility = View.VISIBLE
            binding.btnCreateBus.visibility = View.GONE
            busModel.addBus(this, brand, busno, collegeno)!!.observe(this,
                Observer {
                    if (it != null) {
                        binding.pg.visibility = View.GONE
                        binding.btnCreateBus.visibility = View.VISIBLE
                        if (it.status) {
                            binding.etBrand.requestFocus()
                            binding.etBrand.setText("")
                            binding.etBusNo.setText("")
                            binding.etCollegeno.setText("")
                            Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(this,  it.msg, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                })
        }
    }
}