package com.techfii.bustracking.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import com.techfii.bustracking.models.AdminLogin
import com.techfii.bustracking.models.UpdateResult
import com.techfii.bustracking.repositories.BusTrackingRepository

class LoginViewModel : ViewModel() {

    fun getAdminLogin(
        context: Context,
        username: String,
        password: String,
        user_type: String
    ): LiveData<AdminLogin>? {


        return BusTrackingRepository.getAdminLogin(context, username, password, user_type)
    }

    fun changePassword(
        context: Context,
        id: String,
        new_password: String,
        user_type: String
    ): LiveData<UpdateResult>? {


        return BusTrackingRepository.changePassword(context, id, new_password, user_type)
    }

    fun userlogout(
        context: Context,
        id: String,
        user_type: String
    ): LiveData<UpdateResult>? {
        return BusTrackingRepository.userlogout(context, id, user_type)
    }
}