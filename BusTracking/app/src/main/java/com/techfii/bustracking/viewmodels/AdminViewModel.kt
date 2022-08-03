package com.techfii.bustracking.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techfii.bustracking.models.AdminList
import com.techfii.bustracking.models.DriverList
import com.techfii.bustracking.models.NewUser
import com.techfii.bustracking.repositories.BusTrackingRepository

class AdminViewModel : ViewModel() {

    fun adminList(context: Context): LiveData<AdminList>? {
        return BusTrackingRepository.adminList(context)
    }

    fun addAdmin(
        context: Context,
        username: String,
        mobile: String,
        password: String
    ): LiveData<NewUser>? {
        return BusTrackingRepository.addAdmin(
            context,
            username,
            mobile,
            password
        )
    }
}