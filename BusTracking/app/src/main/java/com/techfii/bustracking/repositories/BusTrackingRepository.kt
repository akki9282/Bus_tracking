package com.techfii.bustracking.repositories

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.grapesberry.paydoh.apis.ApiService
import com.grapesberry.paydoh.apis.ApiUtils
import com.techfii.bustracking.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Driver

object BusTrackingRepository {

    var TAG = "BusTrackingRepository"
    var adminLogin = MutableLiveData<AdminLogin>()

    fun getAdminLogin(
        context: Context,
        username: String,
        password: String,
        user_type: String
    ): MutableLiveData<AdminLogin> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getAdminLogin("login", username, password, user_type)
            ?.enqueue(object :
                Callback<AdminLogin?> {
                override fun onResponse(call: Call<AdminLogin?>, response: Response<AdminLogin?>) {
                    val data = response.body()
                    if (data != null) {
                        adminLogin.value = data!!
                    }
                }

                override fun onFailure(call: Call<AdminLogin?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return adminLogin
    }

    var newDriver = MutableLiveData<NewUser>()
    fun addDriver(
        context: Context,
        username: String,
        password: String,
        mobile: String
    ): MutableLiveData<NewUser> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.addDriver("new_driver", username, mobile, password)
            ?.enqueue(object :
                Callback<NewUser?> {
                override fun onResponse(call: Call<NewUser?>, response: Response<NewUser?>) {
                    val data = response.body()
                    if (data != null) {
                        newDriver.value = data!!
                    }
                }

                override fun onFailure(call: Call<NewUser?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return newDriver
    }

    var newBus = MutableLiveData<NewUser>()
    fun addBus(
        context: Context,
        brand: String,
        bus_no: String,
        college_no: String
    ): MutableLiveData<NewUser> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.addBus("new_bus", brand, bus_no, college_no)
            ?.enqueue(object :
                Callback<NewUser?> {
                override fun onResponse(call: Call<NewUser?>, response: Response<NewUser?>) {
                    val data = response.body()
                    if (data != null) {
                        newBus.value = data!!
                    }
                }

                override fun onFailure(call: Call<NewUser?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return newBus
    }

    var newStudent = MutableLiveData<NewUser>()
    fun addStudent(
        context: Context,
        username: String,
        mobile: String,
        password: String,
        standard: String,
        address: String
    ): MutableLiveData<NewUser> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.addStudent("new_student", username, mobile, password, standard, address)
            ?.enqueue(object :
                Callback<NewUser?> {
                override fun onResponse(call: Call<NewUser?>, response: Response<NewUser?>) {
                    val data = response.body()
                    if (data != null) {
                        newStudent.value = data!!
                    }
                }

                override fun onFailure(call: Call<NewUser?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return newStudent
    }

    var driverList = MutableLiveData<DriverList>()
    fun getDriverList(context: Context): MutableLiveData<DriverList> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getDriverList("driver_list")
            ?.enqueue(object :
                Callback<DriverList?> {
                override fun onResponse(call: Call<DriverList?>, response: Response<DriverList?>) {
                    val data = response.body()
                    if (data != null) {
                        driverList.value = data!!
                    }
                }

                override fun onFailure(call: Call<DriverList?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return driverList
    }

    var busList = MutableLiveData<BusList>()
    fun getBusList(context: Context): MutableLiveData<BusList> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getBusList("bus_list")
            ?.enqueue(object :
                Callback<BusList?> {
                override fun onResponse(call: Call<BusList?>, response: Response<BusList?>) {
                    val data = response.body()
                    if (data != null) {
                        busList.value = data!!
                    }
                }

                override fun onFailure(call: Call<BusList?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return busList
    }

    var studentList = MutableLiveData<StudentList>()
    fun getStudentList(context: Context): MutableLiveData<StudentList> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getStudentList("student_list")
            ?.enqueue(object :
                Callback<StudentList?> {
                override fun onResponse(
                    call: Call<StudentList?>,
                    response: Response<StudentList?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        studentList.value = data!!
                    }
                }

                override fun onFailure(call: Call<StudentList?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return studentList
    }

    var updateBus = MutableLiveData<UpdateResult>()
    fun allocateBus(
        context: Context,
        driver_id: String,
        bus_no: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.allocateBus("allocate_bus", driver_id, bus_no)
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        updateBus.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return updateBus
    }

    var changePassword = MutableLiveData<UpdateResult>()
    fun changePassword(
        context: Context,
        id: String,
        new_password: String,
        user_type: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.changePassword("change_password", id, new_password, user_type)
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        changePassword.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()

                }
            })
        return changePassword
    }

    var getJourneyId = MutableLiveData<JourneyID>()
    fun getLastJourney(context: Context): MutableLiveData<JourneyID> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getLastJourney("get_last_journey")
            ?.enqueue(object :
                Callback<JourneyID?> {
                override fun onResponse(call: Call<JourneyID?>, response: Response<JourneyID?>) {
                    val data = response.body()
                    if (data != null) {
                        getJourneyId.value = data!!
                    }
                }

                override fun onFailure(call: Call<JourneyID?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return getJourneyId
    }

    var driverLogout = MutableLiveData<UpdateResult>()
    fun driverLogout(
        context: Context,
        driver_id: String,
        journey_id: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.driverLogout("driver_logout", driver_id, journey_id)
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        driverLogout.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return driverLogout
    }

    var newJourney = MutableLiveData<UpdateResult>()
    fun newJourney(
        context: Context,
        driver_id: String,
        journey_id: String,
        latitude: String, longitude: String, loginval: String, logout: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.newJourney(
            "new_journey",
            driver_id,
            journey_id,
            latitude,
            longitude,
            loginval,
            logout
        )
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        newJourney.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return newJourney
    }


    var driverStatusList = MutableLiveData<DriverStatusList>()
    fun getDriverStatusData(
        context: Context
    ): MutableLiveData<DriverStatusList> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getDriverStatusData(
            "driver_status_list")
            ?.enqueue(object :
                Callback<DriverStatusList?> {
                override fun onResponse(
                    call: Call<DriverStatusList?>,
                    response: Response<DriverStatusList?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        driverStatusList.value = data!!
                    }
                }

                override fun onFailure(call: Call<DriverStatusList?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return driverStatusList
    }

    var deleteuser = MutableLiveData<UpdateResult>()
    fun deleteuser(
        context: Context,
        colname: String,user_type: String,
        colvalue: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.deleteuser("deleteuser", colname, user_type,colvalue)
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        deleteuser.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return deleteuser
    }


    var userlogout = MutableLiveData<UpdateResult>()
    fun userlogout(
        context: Context,
        id: String,user_type: String
    ): MutableLiveData<UpdateResult> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.userlogout("userlogout", id, user_type)
            ?.enqueue(object :
                Callback<UpdateResult?> {
                override fun onResponse(
                    call: Call<UpdateResult?>,
                    response: Response<UpdateResult?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        userlogout.value = data!!
                    }
                }

                override fun onFailure(call: Call<UpdateResult?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return userlogout
    }
    var driverLocation = MutableLiveData<DriverLocation>()
    fun driverlocation(
        context: Context,
        driver_id: String
    ): MutableLiveData<DriverLocation> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.driverlocation("driver_last_location", driver_id)
            ?.enqueue(object :
                Callback<DriverLocation?> {
                override fun onResponse(
                    call: Call<DriverLocation?>,
                    response: Response<DriverLocation?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        driverLocation.value = data!!
                    }
                }

                override fun onFailure(call: Call<DriverLocation?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return driverLocation
    }
    var adminList = MutableLiveData<AdminList>()
    fun adminList(
        context: Context
    ): MutableLiveData<AdminList> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.getAdminList("admin_list")
            ?.enqueue(object :
                Callback<AdminList?> {
                override fun onResponse(
                    call: Call<AdminList?>,
                    response: Response<AdminList?>
                ) {
                    val data = response.body()
                    if (data != null) {
                        adminList.value = data!!
                    }
                }

                override fun onFailure(call: Call<AdminList?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return adminList
    }

    var newAdmin = MutableLiveData<NewUser>()
    fun addAdmin(
        context: Context,
        username: String,
        mobile: String,
        password: String

    ): MutableLiveData<NewUser> {
        var apiService: ApiService? = ApiUtils.getAPIService()

        apiService?.addAdmin("new_admin", username, mobile, password)
            ?.enqueue(object :
                Callback<NewUser?> {
                override fun onResponse(call: Call<NewUser?>, response: Response<NewUser?>) {
                    val data = response.body()
                    if (data != null) {
                        newAdmin.value = data!!
                    }
                }

                override fun onFailure(call: Call<NewUser?>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show()
                }
            })
        return newAdmin
    }
}