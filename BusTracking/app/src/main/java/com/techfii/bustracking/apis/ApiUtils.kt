package com.grapesberry.paydoh.apis

import com.grapesberry.paydoh.apis.RetrofitClient.getClient
import com.techfii.bustracking.utilities.Constants


object ApiUtils {
    public fun ApiUtils() {}
    fun getAPIService(): ApiService? {
        var BASE_URL = Constants.BASE_URL
        return getClient(BASE_URL)!!.create(ApiService::class.java)
    }
}