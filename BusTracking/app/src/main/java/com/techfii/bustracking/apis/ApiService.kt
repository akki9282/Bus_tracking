package com.grapesberry.paydoh.apis


import com.techfii.bustracking.models.*
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST("/api.php")
    @FormUrlEncoded
    fun getAdminLogin(
        @Field("login") login: String, @Field("username") username: String,
        @Field("password") password: String, @Field("user_type") user_type: String
    ): Call<AdminLogin>

    @POST("/api.php")
    @FormUrlEncoded
    fun addDriver(
        @Field("new_driver") new_driver: String, @Field("name") name: String,
        @Field("mobile") mobile: String, @Field("password") password: String
    ): Call<NewUser>

    @POST("/api.php")
    @FormUrlEncoded
    fun addBus(
        @Field("new_bus") new_bus: String, @Field("brand") brand: String,
        @Field("bus_no") bus_no: String, @Field("college_no") college_no: String
    ): Call<NewUser>


    @POST("/api.php")
    @FormUrlEncoded
    fun addStudent(
        @Field("new_student") new_student: String, @Field("name") name: String,
        @Field("mobile") mobile: String, @Field("password") password: String,
        @Field("standard") standard: String, @Field("address") address: String
    ): Call<NewUser>

    @POST("/api.php")
    @FormUrlEncoded
    fun getDriverList(
        @Field("driver_list") driver_list: String
    ): Call<DriverList>

    @POST("/api.php")
    @FormUrlEncoded
    fun getBusList(
        @Field("bus_list") bus_list: String
    ): Call<BusList>

    @POST("/api.php")
    @FormUrlEncoded
    fun getStudentList(
        @Field("student_list") student_list: String
    ): Call<StudentList>

    @POST("/api.php")
    @FormUrlEncoded
    fun allocateBus(
        @Field("allocate_bus") allocate_bus: String,
        @Field("driver_id") driver_id: String,
        @Field("bus_no") bus_no: String
    ): Call<UpdateResult>

    @POST("/api.php")
    @FormUrlEncoded
    fun changePassword(
        @Field("change_password") change_password: String,
        @Field("id") id: String,
        @Field("new_password") new_password: String,
        @Field("user_type") user_type: String
    ): Call<UpdateResult>


    @POST("/api.php")
    @FormUrlEncoded
    fun getLastJourney(
        @Field("get_last_journey") get_last_journey: String,
    ): Call<JourneyID>


    @POST("/api.php")
    @FormUrlEncoded
    fun driverLogout(
        @Field("driver_logout") driver_logout: String,
        @Field("driver_id") driver_id: String,
        @Field("journey_id") journey_id: String
    ): Call<UpdateResult>

    @POST("/api.php")
    @FormUrlEncoded
    fun newJourney(
        @Field("new_journey") new_journey: String,
        @Field("driver_id") driver_id: String,
        @Field("journey_id") journey_id: String,
        @Field("latitude") latitude: String,
        @Field("longitude") longitude: String,
        @Field("loginval") loginval: String,
        @Field("logout") logout: String
    ): Call<UpdateResult>

    @POST("/api.php")
    @FormUrlEncoded
    fun getDriverStatusData(
        @Field("driver_status_list") driver_status_list: String
    ): Call<DriverStatusList>

    @POST("/api.php")
    @FormUrlEncoded
    fun deleteuser(
        @Field("deleteuser") deleteuser: String,
        @Field("colname") colname: String,
        @Field("user_type") user_type: String,
        @Field("colvalue") colvalue: String
    ): Call<UpdateResult>

    @POST("/api.php")
    @FormUrlEncoded
    fun userlogout(
        @Field("userlogout") userlogout: String,
        @Field("id") id: String,
        @Field("user_type") user_type: String
    ): Call<UpdateResult>


    @POST("/api.php")
    @FormUrlEncoded
    fun driverlocation(
        @Field("driver_last_location") driver_last_location: String,
        @Field("driver_id") driver_id: String
    ): Call<DriverLocation>


    @POST("/api.php")
    @FormUrlEncoded
    fun getAdminList(
        @Field("admin_list") admin_list: String
    ): Call<AdminList>

    @POST("/api.php")
    @FormUrlEncoded
    fun addAdmin(
        @Field("new_admin") new_admin: String,
        @Field("username") username: String,
        @Field("mobile") mobile: String,
        @Field("password") password: String
    ): Call<NewUser>
}