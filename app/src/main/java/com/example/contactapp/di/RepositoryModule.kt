package com.example.contactapp.di

import com.example.contactapp.domain.AppRepository
import com.example.contactapp.domain.impl.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getRepositoryImpl(appRepositoryImpl: AppRepositoryImpl): AppRepository

}