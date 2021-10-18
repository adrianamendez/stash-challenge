package com.stashinvest.stashchallenge.api

import com.stashinvest.stashchallenge.api.model.DetailImageResponse
import com.stashinvest.stashchallenge.api.model.ImageResponse
import com.stashinvest.stashchallenge.api.model.MetadataResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

class StashImageService @Inject constructor(private var api: StashImagesApi) {

    fun searchImages(phrase: String): Single<ImageResponse> {
        return api.searchImages(phrase, FIELDS, SORT_ORDER)
    }

    private fun getImageMetadata(id: String): Single<MetadataResponse> {
        return api.getImageMetadata(id)
    }

    private fun getSimilarImages(id: String): Single<ImageResponse> {
        return api.getSimilarImages(id)
    }

    fun getComposeDetailImages(id: String): Single<DetailImageResponse> {
        return getImageMetadata(id).zipWith(
            getSimilarImages(id),
            BiFunction(::DetailImageResponse)
        )
    }

    companion object {
        val FIELDS = "id,title,thumb"
        val SORT_ORDER = "best"
    }
}
