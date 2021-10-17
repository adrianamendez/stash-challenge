package com.stashinvest.stashchallenge.ui.fragment.uimodel

import android.os.Parcelable
import com.stashinvest.stashchallenge.api.model.ImageResult
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageUi(
    val id: String,
    val title: String,
    val displaySizes: List<DisplaySizeUi>,
    val thumbUri: String?
) : Parcelable {

    companion object {
        fun mapFromDomain(imageResult: ImageResult) = ImageUi(
            imageResult.id,
            imageResult.title,
            imageResult.displaySizes.map { DisplaySizeUi.mapFromDomain(it) },
            imageResult.thumbUri
        )
    }
}