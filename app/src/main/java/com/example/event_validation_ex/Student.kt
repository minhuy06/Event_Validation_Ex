package com.example.event_validation_ex

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val studentId: String,
    val className: String
) : Parcelable