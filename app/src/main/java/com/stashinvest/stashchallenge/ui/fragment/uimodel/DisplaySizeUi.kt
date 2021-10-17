package com.stashinvest.stashchallenge.ui.fragment.uimodel

import android.os.Parcelable
import com.stashinvest.stashchallenge.api.model.DisplaySize
import kotlinx.android.parcel.Parcelize

@Parcelize
class DisplaySizeUi(
    val isWatermarked: Boolean,
    val name: String,
    val uri: String
) : Parcelable {

    companion object {

        fun mapFromDomain(displaySize: DisplaySize) = DisplaySizeUi(
            displaySize.isWatermarked,
            displaySize.name,
            displaySize.uri
        )
    }
}