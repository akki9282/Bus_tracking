package com.techfii.bustracking.models

data class DriverStatusList(
    val DriverStatusData: List<DriverStatusData>,
    val msg: String,
    val status: Boolean
)