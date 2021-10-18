package com.stashinvest.stashchallenge.api

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class RxScheduler @Inject constructor() : IScheduler {

    override fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
