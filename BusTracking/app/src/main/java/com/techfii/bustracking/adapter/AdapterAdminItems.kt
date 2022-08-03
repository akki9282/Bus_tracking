package com.techfii.bustracking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.techfii.bustracking.AdminListActivity
import com.techfii.bustracking.BusTrackingApplication
import com.techfii.bustracking.databinding.LayoutItemListBinding
import com.techfii.bustracking.models.AdminData
import java.util.*
import kotlin.collections.ArrayList

class AdapterAdminItems : RecyclerView.Adapter<AdapterAdminItems.ViewHolder>(), Filterable {

    var context: Context = BusTrackingApplication.instance.applicationContext
    private var TAG: String = "AdapterItems"


    companion object {
        var tempList: List<AdminData> = ArrayList<AdminData>()
        var list: List<AdminData> = ArrayList<AdminData>()
    }

    fun setList(mList: List<AdminData>) {
        list = mList
        tempList = list

    }


    inner class ViewHolder(val binding: LayoutItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder) {
            if (list[position].username.isNullOrEmpty()) {
                binding.container.visibility = View.GONE
            } else {
                binding.container.visibility = View.VISIBLE
            }
            binding.idName.setText(list[position].username)
            binding.idSecondaryName.setText("")
            binding.idCollege.setText("")
            binding.idMobile.setText(list[position].mobile)
            binding.delete.setOnClickListener {
                if (AdminListActivity.deleteListener != null) {
                    AdminListActivity.deleteListener.onDelete(list[position].username)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) {
                    list = tempList
                } else {
                    val resultList = ArrayList<AdminData>()
                    for (row in tempList) {
                        if (row.username.lowercase(Locale.ROOT)
                                .contains(charString.lowercase(Locale.ROOT))
                            || row.mobile.lowercase(Locale.ROOT)
                                .contains(charString.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    list = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = list
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<AdminData>
                notifyDataSetChanged()
            }
        }
    }


}