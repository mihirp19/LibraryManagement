package com.example.librarymanagement.models

data class Book(
    val bookUid: String = "",
    val name: String = "",
    val author: String = "",
    val year: String = "",
    val category: String = "",
    val description: String = "",
    val issuedFrom: String = "",
    val issuedBy: String = "",
    val issuedAt: Long = System.currentTimeMillis(),
    val issuedTill: Long = 0L
)
