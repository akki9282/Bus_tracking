package com.techfii.bustracking

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.techfii.bustracking.adapter.AdapterDriverActiveStatus
import com.techfii.bustracking.adapter.AdapterDriverItems
import com.techfii.bustracking.adapter.AdapterDriverNonActiveStatus
import com.techfii.bustracking.databinding.ActivityAdminBinding
import com.techfii.bustracking.databinding.ActivityStudentBinding
import com.techfii.bustracking.models.DriverData
import com.techfii.bustracking.models.DriverStatusData
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.DriverViewModel
import com.techfii.bustracking.viewmodels.LoginViewModel

class StudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    lateinit var preferences: SavePreferences
    lateinit var driverModel: DriverViewModel
    lateinit var loginModel: LoginViewModel
    var adapterActive = AdapterDriverActiveStatus()
    var adapterNonActive = AdapterDriverNonActiveStatus()
    var mActiveList = ArrayList<DriverStatusData>()
    var mNonActiveList = ArrayList<DriverStatusData>()

    companion object {
        fun startIntent(context: Context) {
            val i = Intent(context, StudentActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(i)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        driverModel = ViewModelProvider(this).get(DriverViewModel::class.java)
        loginModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        setListeners()
    }

    fun setListeners() {
        binding.ivChangePwd.setOnClickListener {
            ChangePasswordActivity.startIntent(this)
        }
        binding.ivLogout.setOnClickListener {
            logout()
        }
    }

    fun logout() {
        loginModel.userlogout(this, preferences.getUserid().toString(), "student")!!.observe(this,
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

    override fun onResume() {
        super.onResume()
        getList()
    }

    fun getList() {
        binding.pg.visibility = View.VISIBLE
        driverModel.getDriverStatusData(this)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    if (it.DriverStatusData != null) {
                        if (it.status) {
                            var list = it.DriverStatusData
                            setList(list)
                        } else {
                            Toast.makeText(
                                this,
                                "Sorry,Could not load Drivers",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
    }

    fun setList(list: List<DriverStatusData>) {
        mActiveList.clear()
        mNonActiveList.clear()
        for (obj in list) {
            if (obj.logout == null) {
                mNonActiveList.add(obj)
            } else {
                if (obj.logout.toInt() == 1) {
                    mNonActiveList.add(obj)
                } else {
                    mActiveList.add(obj)
                }
            }

        }

        binding.rvActiveList.layoutManager = LinearLayoutManager(this)
        binding.rvActiveList.adapter = adapterActive
        adapterActive.setList(mActiveList)
        adapterActive.notifyDataSetChanged()

        binding.rvNonActiveList.layoutManager = LinearLayoutManager(this)
        binding.rvNonActiveList.adapter = adapterNonActive
        adapterNonActive.setList(mNonActiveList)
        adapterNonActive.notifyDataSetChanged()

    }
}