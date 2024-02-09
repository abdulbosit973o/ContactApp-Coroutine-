package com.example.contactapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.contactapp.data.local.dao.ContactDao
import com.example.contactapp.data.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao


    companion object{
        private lateinit var instance : MyDatabase
        fun init(context: Context) {
            if (!(::instance.isInitialized))
                instance = Room.databaseBuilder(context, MyDatabase::class.java, "MyContacts.db")
                    .allowMainThreadQueries()
                    .build()
        }

        fun getInstance() = instance
    }

}