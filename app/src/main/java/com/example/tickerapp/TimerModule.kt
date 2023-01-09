package com.example.tickerapp

import com.example.tickerlibrary.Ticker
import com.example.tickerlibrary.TickerImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {

    @Provides
    fun provideTicker() : Ticker {
        return TickerImp
    }
}