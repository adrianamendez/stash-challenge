package com.stashinvest.stashchallenge.injection

import com.stashinvest.stashchallenge.api.IScheduler
import com.stashinvest.stashchallenge.api.RxScheduler
import com.stashinvest.stashchallenge.util.ILogger
import com.stashinvest.stashchallenge.util.LogcatLogger
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Singleton
    @Binds
    abstract fun bindScheduler(impl: RxScheduler): IScheduler

    @Singleton
    @Binds
    abstract fun bindLogger(impl: LogcatLogger): ILogger
}