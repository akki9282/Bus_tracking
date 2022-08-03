package com.techfii.bustracking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.techfii.bustracking.BusTrackingApplication
import com.techfii.bustracking.MapActivity
import com.techfii.bustracking.databinding.LayoutActiveBusBinding
import com.techfii.bustracking.models.DriverData
import com.techfii.bustracking.models.DriverStatusData
import java.util.*
import kotlin.collections.ArrayList

class AdapterDriverActiveStatus : RecyclerView.Adapter<AdapterDriverActiveStatus.ViewHolder>() {

    var context: Context = BusTrackingApplication.instance.applicationContext
    private var TAG: String = "AdapterItems"


    companion object {

        var list: List<DriverStatusData> = ArrayList<DriverStatusData>()
    }

    fun setList(mList: List<DriverStatusData>) {
        list = mList

    }


    inner class ViewHolder(val binding: LayoutActiveBusBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutActiveBusBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            if (list[position].name.isNullOrEmpty()) {
                binding.container.visibility = View.GONE
            } else {
                binding.container.visibility = View.VISIBLE
            }
            binding.idName.setText(list[position].name)
            if (!list[position].bus_no.isNullOrEmpty()) {
                binding.idSecondaryName.setText("Bus No. ${list[position].bus_no}")
            } else {
                binding.idSecondaryName.setText("")
            }
            if (!list[position].college_no.isNullOrEmpty()) {
                binding.idCollege.setText("College No. ${list[position].college_no}")
            } else {
                binding.idCollege.setText("")
            }
            binding.idMobile.setText(list[position].mobile)

            binding.container.setOnClickListener {
                if (list[position].latitude != null && list[position].longitude != null) {
                    val intent = Intent(context,MapActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("latitude",list[position].latitude)
                    intent.putExtra("longitude",list[position].longitude)
                    intent.putExtra("driver_id",list[position].id)
                    context.startActivity(intent)
                }
                else{
                    Toast.makeText(context,"Driver journey not yet started",Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size

    }


}