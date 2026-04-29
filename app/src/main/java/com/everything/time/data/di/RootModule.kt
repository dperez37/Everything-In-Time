package com.everything.time.data.di

import com.everything.time.data.manager.KotlinTimeManager
import com.everything.time.data.manager.KotlinTimeManagerImpl
import com.everything.time.ui.util.JavaClock
import com.everything.time.ui.util.KotlinClock
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RootModule {
    @Provides
    @Singleton
    fun provideJavaClock(): JavaClock = Clock.systemDefaultZone()

    @Provides
    @Singleton
    fun provideKotlinClock(): KotlinClock = kotlin.time.Clock.System

    @Provides
    @Singleton
    fun provideKotlinTimeZoneManager(
        clock: KotlinClock,
    ): KotlinTimeManager = KotlinTimeManagerImpl(
        clock = clock,
    )
}
