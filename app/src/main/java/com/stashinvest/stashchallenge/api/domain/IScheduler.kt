package com.stashinvest.stashchallenge.api.domain

import io.reactivex.rxjava3.core.SingleTransformer

interface IScheduler {
    fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T>
}