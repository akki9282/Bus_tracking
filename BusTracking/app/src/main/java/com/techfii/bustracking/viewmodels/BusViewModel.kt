package com.techfii.bustracking.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techfii.bustracking.models.BusList
import com.techfii.bustracking.models.DriverList
import com.techfii.bustracking.models.NewUser
import com.techfii.bustracking.models.UpdateResult
import com.techfii.bustracking.repositories.BusTrackingRepository

class BusViewModel:ViewModel() {

    fun addBus(
        context: Context,
        brand: String,
        bus_no: String,
        college_no: String
    ): LiveData<NewUser>? {


        return BusTrackingRepository.addBus(context, brand, bus_no, college_no)
    }
    fun getBusList(context: Context):LiveData<BusList>?{
        return  BusTrackingRepository.getBusList(context)
    }
    fun allocateBus(context: Context,driver_id:String , bus_no:String):LiveData<UpdateResult>?{
        return  BusTrackingRepository.allocateBus(context,driver_id,bus_no)
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