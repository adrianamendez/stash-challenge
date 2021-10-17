package com.stashinvest.stashchallenge.injection

import com.stashinvest.stashchallenge.api.domain.ILogger
import com.stashinvest.stashchallenge.api.domain.IScheduler
import com.stashinvest.stashchallenge.api.domain.LogcatLogger
import com.stashinvest.stashchallenge.api.domain.RxScheduler
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