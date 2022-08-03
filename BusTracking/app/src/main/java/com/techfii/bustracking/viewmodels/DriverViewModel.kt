package com.techfii.bustracking.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.techfii.bustracking.models.*
import com.techfii.bustracking.repositories.BusTrackingRepository

class DriverViewModel : ViewModel() {
    fun addDriver(
        context: Context,
        username: String,
        password: String,
        mobile: String
    ): LiveData<NewUser>? {

        return BusTrackingRepository.addDriver(context, username, password, mobile)
    }

    fun getDriverList(context: Context): LiveData<DriverList>? {
        return BusTrackingRepository.getDriverList(context)
    }

    fun getLastJourney(context: Context): LiveData<JourneyID>? {
        return BusTrackingRepository.getLastJourney(context)
    }

    fun driverLoagout(
        context: Context,
        driver_id: String,
        journey_id: String
    ): LiveData<UpdateResult>? {
        return BusTrackingRepository.driverLogout(context, driver_id, journey_id)
    }

    fun newJourney(
        context: Context,
        driver_id: String,
        journey_id: String,
        latitude: String,
        longitude: String,
        loginval: String,
        logout: String
    ): LiveData<UpdateResult>? {
        return BusTrackingRepository.newJourney(
            context, driver_id, journey_id, latitude,
            longitude,
            loginval,
            logout
        )
    }

    fun getDriverStatusData(context: Context): LiveData<DriverStatusList>? {
        return BusTrackingRepository.getDriverStatusData(context)
    }
    fun deleteuser(
        context: Context,
        colname: String,
        user_type: String,
        colvalue: String
    ): LiveData<UpdateResult>? {
        return BusTrackingRepository.deleteuser(context, colname, user_type, colvalue)
    }

    fun driverLocation(context: Context,driver_id:String): LiveData<DriverLocation>? {
        return BusTrackingRepository.driverlocation(context,driver_id)
    }
}