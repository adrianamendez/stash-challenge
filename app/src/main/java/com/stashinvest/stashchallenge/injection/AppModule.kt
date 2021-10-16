package com.stashinvest.stashchallenge.injection

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context {
        return application
    }
}