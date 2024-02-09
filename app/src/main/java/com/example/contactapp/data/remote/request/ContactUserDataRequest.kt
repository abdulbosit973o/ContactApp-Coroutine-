package com.example.contactapp.data.remote.request

data class ContactUserDataRequest(
    val firstName:String,
    val lastName:String,
    val phone:String,
    val password:String
)