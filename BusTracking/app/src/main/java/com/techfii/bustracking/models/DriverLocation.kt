package com.techfii.bustracking.models

data class DriverLocation(
    val latitude: String,
    val longitude: String,
    val msg: String,
    val status: Boolean
)