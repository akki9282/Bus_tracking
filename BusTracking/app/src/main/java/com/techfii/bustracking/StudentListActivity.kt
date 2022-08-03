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
import com.techfii.bustracking.adapter.AdapterStudentItems
import com.techfii.bustracking.databinding.ActivityStudentListBinding
import com.techfii.bustracking.interfaces.DeleteListeners
import com.techfii.bustracking.models.StudentData
import com.techfii.bustracking.models.StudentList
import com.techfii.bustracking.utilities.SavePreferences
import com.techfii.bustracking.viewmodels.StudentViewModel

class StudentListActivity : AppCompatActivity(), DeleteListeners {

    private lateinit var binding: ActivityStudentListBinding
    lateinit var preferences: SavePreferences
    var adapter = AdapterStudentItems()
    lateinit var studentModel: StudentViewModel

    companion object {
        lateinit var deleteListener: DeleteListeners
        fun startIntent(context: Context) {
            context.startActivity(Intent(context, StudentListActivity::class.java))
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preferences = SavePreferences(this)
        deleteListener = this
        studentModel = ViewModelProvider(this).get(StudentViewModel::class.java)

        setListeners()
    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    fun setListeners() {
        binding.ivAdd.setOnClickListener {
            StudentFormActivity.startIntent(this)
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
        studentModel.getStudentList(this)!!.observe(this,
            Observer {
                if (it != null) {
                    binding.pg.visibility = View.GONE
                    if (it.status) {
                        var list = it.list
                        setStudentList(list)
                    } else {
                        Toast.makeText(
                            this,
                            "Sorry,Could not load Students",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    fun setStudentList(list: List<StudentData>) {

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter


        adapter.setList(list)
        adapter.notifyDataSetChanged()
    }

    override fun onDelete(data: String) {
        binding.pg.visibility = View.VISIBLE
        studentModel.deleteuser(this,"mobile","student",data)!!.observe(this,
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
                            "Sorry,Could not delete Student",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}