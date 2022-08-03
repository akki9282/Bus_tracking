package com.techfii.bustracking.models

data class AdminList(
    val list: List<AdminData>,
    val msg: String,
    val status: Boolean
)