package com.example.librarymanagement.models

data class User(
    val uid: String = "",
    val username: String = "",
    val enrolmentNumber: String = "",
    val email: String = "",
    val role: String = "student"
)
