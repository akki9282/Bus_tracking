package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.techfii.bustracking.adapter.AdapterDriverItems
import com.techfii.bustracking.databinding.ActivityDriverListBinding
import com.techfii.bustracking.interfaces.DeleteListeners
import com.techfii.bustracking.models.DriverData
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.DriverViewModel

class DriverListActivity : AppCompatActivity() ,DeleteListeners{

    private lateinit var binding: ActivityDriverListBinding
    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel
    var adapter = AdapterDriverItems()

    companion object {
        lateinit var deleteListener: DeleteListeners
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, DriverListActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        deleteListener = this
        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)


        setListeners()

    }

    override fun onResume() {
        super.onResume()
        getList()
    }


    fun setListeners() {
        binding.ivAdd.setOnClickListener {
            DriverFormActivity.startIntent(this)
        }
        binding.search.setOnClickListener {
            binding.title.visibility = View.GONE
            binding.search.visibility = View.GONE
            binding.ivAdd.visibility = View.GONE

            binding.frmSearch.visibility = View.VISIBLE
            binding.etSearch.requestFocus()
        }
        binding.hideSearch.setOnClickListener {
            binding.title.visibility = View.VISIBLE
            binding.search.visibility = View.VISIBLE
            binding.ivAdd.visibility = View.VISIBLE

            binding.frmSearch.visibility = View.GONE
        }
        binding.etSearch.addTextChangedListener {
            adapter.filter.filter(it.toString())
        }
    }

    fun getList() {
        binding.pg.visibility = View.VISIBLE
        driverModel.getDriverList(this)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    if (it.status) {
                        var list = it.list
                        setDriverList(list)
                    } else {
                        Toast.makeText(
                            this,
                            "Sorry,Could not load drivers",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun setDriverList(list: List<DriverData>) {

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter


        adapter.setList(list)
        adapter.notifyDataSetChanged()
    }

    override fun onDelete(data: String) {
        binding.pg.visibility = View.VISIBLE
        driverModel.deleteuser(this,"mobile","driver",data)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    if (it.status) {
                        getList()
                        Toast.makeText(
                            this,
                            it.msg,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Sorry,Could not delete Driver",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}