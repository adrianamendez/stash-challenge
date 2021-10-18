package com.stashinvest.stashchallenge.api

import com.stashinvest.stashchallenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class StashImageInterceptor : Interceptor {
    @Inject
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .header("Api-Key", BuildConfig.API_KEY)
            .build()

        return chain.proceed(request)
    }
}
