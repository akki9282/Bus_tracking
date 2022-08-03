package com.techfii.bustracking.models

data class DriverList(
    val list: List<DriverData>,
    val msg: String,
    val status: Boolean
)