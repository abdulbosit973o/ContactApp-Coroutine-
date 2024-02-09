package com.example.contactapp.data.mapper

import com.example.contactapp.data.local.entity.ContactEntity
import com.example.contactapp.data.model.ContactUIData
import com.example.contactapp.data.model.StatusEnum
import com.example.contactapp.data.model.toStatusEnum
import com.example.contactapp.data.remote.response.ContactResponse

object ContactMapper {

    fun ContactResponse.toUIData() : ContactUIData =
        ContactUIData(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = StatusEnum.SYNC
        )

    fun ContactEntity.toUIData(id : Int) : ContactUIData =
        ContactUIData(
            id = id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = this.statusCode.toStatusEnum()
        )
}

