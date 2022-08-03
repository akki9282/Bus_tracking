package com.techfii.bustracking.models

data class StudentList(
    val list: List<StudentData>,
    val msg: String,
    val status: Boolean
)