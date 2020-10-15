package com.aveworks.eagle.di

import com.aveworks.eagle.Analytics
import com.aveworks.eagle.AnalyticsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AnalyticsModule{

    @Singleton
    @Provides
    fun provideAnalytics(): Analytics{
        return AnalyticsImpl()
    }
}