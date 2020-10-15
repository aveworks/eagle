package com.aveworks.eagle.di

import com.aveworks.eagle.Analytics
import com.aveworks.eagle.AnalyticsImpl
import com.aveworks.eagle.api.BlockchainService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule{

    @Singleton
    @Provides
    fun provideBlockchainService(): BlockchainService{
        return BlockchainService.create()
    }
}