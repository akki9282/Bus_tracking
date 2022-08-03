package com.techfii.bustracking.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.techfii.bustracking.BusTrackingApplication
import com.techfii.bustracking.StudentListActivity
import com.techfii.bustracking.databinding.LayoutItemListBinding
import com.techfii.bustracking.models.StudentData
import java.util.*
import kotlin.collections.ArrayList

class AdapterStudentItems : RecyclerView.Adapter<AdapterStudentItems.ViewHolder>(), Filterable {

    var context: Context = BusTrackingApplication.instance.applicationContext
    private var TAG: String = "AdapterItems"


    companion object {
        var tempList: List<StudentData> = ArrayList<StudentData>()
        var list: List<StudentData> = ArrayList<StudentData>()
    }

    fun setList(mList: List<StudentData>) {
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
            if (list[position].name.isNullOrEmpty()) {
                binding.container.visibility = View.GONE
            } else {
                binding.container.visibility = View.VISIBLE
            }
            binding.idName.setText(list[position].name)
            binding.idSecondaryName.setText("${list[position].standard}")
            //    binding.idCollege.setText("College No. ${list[position].college}")
            binding.idMobile.setText(list[position].mobile)
            binding.delete.setOnClickListener {
                if (StudentListActivity.deleteListener != null) {
                    StudentListActivity.deleteListener.onDelete(list[position].mobile)
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
                    val resultList = ArrayList<StudentData>()
                    for (row in tempList) {
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charString.lowercase(Locale.ROOT))
                            || row.standard.lowercase(Locale.ROOT)
                                .contains(charString.lowercase(Locale.ROOT))
                            || row.standard.lowercase(Locale.ROOT)
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
                    results.values as ArrayList<StudentData>
                notifyDataSetChanged()
            }
        }
    }
}