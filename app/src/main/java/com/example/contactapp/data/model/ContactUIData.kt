package com.example.contactapp.data.model

data class ContactUIData(
    val id:Int,
    val firstName:String,
    val lastName:String,
    val phone: String,
    val status: StatusEnum
)
