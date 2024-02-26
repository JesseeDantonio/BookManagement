package com.example.bookmanagement

data class Book(
    val rang: Int,
    val typeDeDocument: String?,
    val reservations: Double,
    val titre: String,
    val auteur: String?
)
