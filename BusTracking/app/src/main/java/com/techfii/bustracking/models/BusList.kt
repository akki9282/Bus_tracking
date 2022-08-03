package com.techfii.bustracking.models

data class BusList(
    val list: List<BusData>,
    val msg: String,
    val status: Boolean
)