package com.techfii.bustracking

import android.R
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techfii.bustracking.databinding.ActivityBusAllocationBinding
import com.techfii.bustracking.databinding.ActivityStudentBinding
import com.techfii.bustracking.models.BusData
import com.techfii.bustracking.models.DriverData
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.BusViewModel
import com.techfii.bustracking.viewmodels.DriverViewModel

class BusAllocationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityBusAllocationBinding
    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel
    lateinit var busModel: BusViewModel
    var objDriverList = ArrayList<DriverData>()
    var objBusList = ArrayList<BusData>()
    var busList = ArrayList<String>()
    var driverList = ArrayList<String>()
    var mDriver = ""
    var mBus = ""
    var mDriverId = 0
    var mBusId = 0
    var mDriverAlloactionId = ""

    companion object {
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, BusAllocationActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBusAllocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        busModel = ViewModelProvider(this).get(BusViewModel::class.java)

        setViews()
        setListeners()
    }

    fun setListeners() {
        binding.driverPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mDriverId = position
                mDriver = driverList.get(position)
            }

        }
        binding.busPicker.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBusId = position
                mBus = busList.get(position)
            }

        }
        binding.btnBusAllocate.setOnClickListener {
            if (mDriverId == 0 || mBusId == 0) {
                Toast.makeText(this, "Please select proper bus and drivr", Toast.LENGTH_LONG).show()
            } else {
                try {
                    binding.pg.visibility = View.VISIBLE
                    binding.btnBusAllocate.visibility = View.GONE
                    mDriverAlloactionId = objDriverList[mDriverId - 1].id
                    busModel.allocateBus(this, mDriverAlloactionId, mBus)!!.observe(this,
                        Observer {
                            if (it != null) {
                                binding.pg.visibility = View.GONE
                                binding.btnBusAllocate.visibility = View.VISIBLE
                                if (it.status) {
                                    Toast.makeText(this, it.msg, Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Sorry,Could not allocate bus",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        })
                } catch (ex: Exception) {
                    Log.d("bus allocation", ex.toString())
                }
            }
        }
    }

    fun setViews() {
        binding.pg.visibility = View.VISIBLE
        driverModel.getDriverList(this)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    if (it.status) {
                        objDriverList.clear()
                        objDriverList = it.list as ArrayList<DriverData>
                        setDrivers()
                    }
                }
            })
        busModel.getBusList(this)!!.observe(this,
            Observer {
                if (it != null) {
                    if (it.status) {
                        objBusList.clear()
                        objBusList = it.list as ArrayList<BusData>
                        setBuses()
                    }
                }
            })


    }

    fun setDrivers() {
        driverList.clear()
        driverList.add(0, "Select Driver")
        for (obj in objDriverList) {
            driverList.add(obj.name)
        }
        binding.driverPicker.adapter =
            ArrayAdapter(this, R.layout.simple_list_item_1, driverList)

    }

    fun setBuses() {
        busList.clear()
        busList.add(0, "Select Bus")
        for (obj in objBusList) {
            busList.add(obj.bus_no)
        }
        binding.busPicker.adapter =
            ArrayAdapter(this, R.layout.simple_list_item_1, busList)

    }
}