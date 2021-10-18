package utils

import com.stashinvest.stashchallenge.api.IScheduler
import io.reactivex.rxjava3.core.SingleTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

class TestScheduler : IScheduler {

    override fun <T> applySingleDefaultSchedulers(): SingleTransformer<T, T> {
        return SingleTransformer { upstream ->
            upstream.subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline())
        }
    }
}
