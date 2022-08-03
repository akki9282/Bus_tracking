package com.techfii.bustracking.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techfii.bustracking.models.NewUser
import com.techfii.bustracking.models.StudentData
import com.techfii.bustracking.models.StudentList
import com.techfii.bustracking.models.UpdateResult
import com.techfii.bustracking.repositories.BusTrackingRepository

class StudentViewModel : ViewModel() {
    fun addStudent(
        context: Context,
        username: String,
        mobile: String,
        password: String,
        standard: String,
        address: String

    ): LiveData<NewUser>? {


        return BusTrackingRepository.addStudent(
            context,
            username,
            mobile,
            password,
            standard,
            address
        )
    }

    fun getStudentList(context: Context): LiveData<StudentList>? {
        return BusTrackingRepository.getStudentList(context)
    }

    fun deleteuser(
        context: Context,
        colname: String,
        user_type: String,
        colvalue: String
    ): LiveData<UpdateResult>? {
        return BusTrackingRepository.deleteuser(context, colname, user_type, colvalue)
    }
}