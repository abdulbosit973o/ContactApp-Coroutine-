package com.example.contactapp.di

import android.content.Context
import androidx.room.Room
import com.example.contactapp.data.local.dao.ContactDao
import com.example.contactapp.data.local.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModule {

    @[Provides Singleton]
    fun provideAppDatabase(@ApplicationContext context: Context) : MyDatabase =
        Room.databaseBuilder(context, MyDatabase::class.java, "MyContact.db")
            .allowMainThreadQueries()
            .build()


    @[Provides Singleton]
    fun provideContactDao(database: MyDatabase) : ContactDao = database.getContactDao()
}