package com.example.contactapp.data.remote.request

data class ContactEditData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String
)