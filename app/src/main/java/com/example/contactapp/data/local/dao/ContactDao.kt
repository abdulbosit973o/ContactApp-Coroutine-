package com.example.contactapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.contactapp.data.local.entity.ContactEntity

@Dao
interface ContactDao {
    @Query("Select * from contactentity")
    fun getAllContacts() : List<ContactEntity>

    @Insert
    fun addContact(contactEntity: ContactEntity)

    @Delete
    fun deleteContact(contactEntity: ContactEntity)
}